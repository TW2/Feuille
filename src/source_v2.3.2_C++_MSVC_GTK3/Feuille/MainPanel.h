#pragma once

#include <gtk/gtk.h>
#include "Language.h"

class MainPanel {
public:
	// Constructeur
	MainPanel();

	// Destructeur
	~MainPanel();

	// Le composant principal qu'on veut en sortie en tant que panel
	static void configure_panel(GtkWidget* container, std::vector<LanguageLinkage> language);
};