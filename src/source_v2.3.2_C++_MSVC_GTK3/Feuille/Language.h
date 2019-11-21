#pragma once

#include <gtk/gtk.h>
#include <map>
#include "dirent.h"

class Language {
public:
	static std::map<const gchar*, const gchar*> load_language(const gchar* iso);
	static const gchar* get_content(std::map<const gchar*, const gchar*> language, const gchar* key, const gchar* default_value);
private:
	static bool contains_file(const gchar* folder, const gchar* filename);
	static std::map<const gchar*, const gchar*> get_from_path(const gchar* path);
};