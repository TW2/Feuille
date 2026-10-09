cmake_minimum_required(VERSION 3.10.0)
project(assa VERSION 0.1.0 LANGUAGES C CXX)

find_package(PkgConfig REQUIRED)
pkg_check_modules(LIBASS REQUIRED IMPORTED_TARGET libass)

add_library(assa assa.cpp)
target_link_libraries(assa PRIVATE PkgConfig::LIBASS)
