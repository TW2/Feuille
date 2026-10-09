# assa

`assa` is a C++ static library built with CMake. It links against
[libass](https://github.com/libass/libass), which is declared as a dependency
in the vcpkg manifest.

## Prerequisites

- CMake 3.10 or newer
- A C/C++ compiler and the platform's build tools
- [vcpkg](https://learn.microsoft.com/vcpkg/) with `VCPKG_ROOT` set to its
  installation directory
- `pkg-config` (provided by `pkgconf` on some platforms)

The CMake project uses pkg-config to locate libass. Ensure the executable is
available on `PATH`; with vcpkg, use its toolchain file so the manifest
dependency is installed and discoverable.

## Linux

For Debian or Ubuntu, install the build tools and pkg-config:

```sh
sudo apt update
sudo apt install build-essential cmake pkg-config
```

Configure and build with the vcpkg toolchain:

```sh
cmake -S . -B build \
  -DCMAKE_TOOLCHAIN_FILE="$VCPKG_ROOT/scripts/buildsystems/vcpkg.cmake" \
  -DVCPKG_TARGET_TRIPLET=x64-linux \
  -DCMAKE_BUILD_TYPE=Release
cmake --build build --config Release
```

The static library is produced at `build/libassa.a`.

## Windows

Install Visual Studio 2022 with the **Desktop development with C++** workload,
CMake, vcpkg, and a `pkg-config`-compatible executable on `PATH`. Then, from a
Developer PowerShell with `VCPKG_ROOT` set, run:

```powershell
cmake -S . -B build `
  -DCMAKE_TOOLCHAIN_FILE="$env:VCPKG_ROOT/scripts/buildsystems/vcpkg.cmake" `
  -DVCPKG_TARGET_TRIPLET=x64-windows
cmake --build build --config Release
```

With the Visual Studio generator, the static library is typically produced at
`build/Release/assa.lib`.

## macOS

Install Xcode Command Line Tools, CMake, and pkg-config:

```sh
xcode-select --install
brew install cmake pkgconf
```

Configure and build for Apple Silicon:

```sh
cmake -S . -B build \
  -DCMAKE_TOOLCHAIN_FILE="$VCPKG_ROOT/scripts/buildsystems/vcpkg.cmake" \
  -DVCPKG_TARGET_TRIPLET=arm64-osx \
  -DCMAKE_BUILD_TYPE=Release
cmake --build build --config Release
```

For an Intel Mac, use `x64-osx` instead of `arm64-osx`. The static library is
produced at `build/libassa.a`.

## Static and shared builds

This project follows CMake's `BUILD_SHARED_LIBS` option. By default, the target
is built as a static library. To build it as a shared library, configure with
`-DBUILD_SHARED_LIBS=ON` and use a dedicated build directory so the option does
not reuse a prior static-library configuration.

Linux:

```sh
cmake -S . -B build-shared \
  -DCMAKE_TOOLCHAIN_FILE="$VCPKG_ROOT/scripts/buildsystems/vcpkg.cmake" \
  -DVCPKG_TARGET_TRIPLET=x64-linux \
  -DBUILD_SHARED_LIBS=ON \
  -DCMAKE_BUILD_TYPE=Release
cmake --build build-shared --config Release
```

Windows (Developer PowerShell):

```powershell
cmake -S . -B build-shared `
  -DCMAKE_TOOLCHAIN_FILE="$env:VCPKG_ROOT/scripts/buildsystems/vcpkg.cmake" `
  -DVCPKG_TARGET_TRIPLET=x64-windows `
  -DBUILD_SHARED_LIBS=ON
cmake --build build-shared --config Release
```

macOS (use `x64-osx` instead of `arm64-osx` for Intel):

```sh
cmake -S . -B build-shared \
  -DCMAKE_TOOLCHAIN_FILE="$VCPKG_ROOT/scripts/buildsystems/vcpkg.cmake" \
  -DVCPKG_TARGET_TRIPLET=arm64-osx \
  -DBUILD_SHARED_LIBS=ON \
  -DCMAKE_BUILD_TYPE=Release
cmake --build build-shared --config Release
```

The shared library output is typically `libassa.so` on Linux, `assa.dll`
(with an import library `assa.lib`) on Windows, and `libassa.dylib` on macOS.
The exact location depends on the CMake generator. This project currently
defines a build target only; it does not install the library or its headers.
