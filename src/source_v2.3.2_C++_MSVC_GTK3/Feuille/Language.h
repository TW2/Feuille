#pragma once

#include <gtk/gtk.h>
#include "dirent.h"
#include "LanguageLinkage.h"
#include <vector>

class Language {
public:
	// Constructeur
	Language();

	// Destructeur
	~Language();

	static std::vector<LanguageLinkage> load_language(const gchar* iso);
	static const gchar* get_content(std::vector<LanguageLinkage> llink, const gchar* key, const gchar* default_value);
private:
	static bool contains_file(const gchar* folder, const gchar* filename);
	static std::vector<LanguageLinkage> get_from_path(const gchar* path);
};