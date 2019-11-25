#pragma once

#include <gtk/gtk.h>
#include "Language.h"

class ChatPanel {
public:
	// Constructeur
	ChatPanel();

	// Destructeur
	~ChatPanel();

	// Le composant principal qu'on veut en sortie en tant que panel
	static void configure_panel(GtkWidget* container, std::vector<LanguageLinkage> language);
};