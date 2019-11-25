#pragma once

#include <gtk/gtk.h>

class LanguageLinkage {
public:
	// Constructeur
	LanguageLinkage();

	// Destructeur
	~LanguageLinkage();

	void set_key(const gchar* key);
	const gchar* get_key();
	void set_value(const gchar* value);
	const gchar* get_value();
};