#pragma once

#include <gtk/gtk.h>
#include <map>

class ChatPanel {
public:
	// Le composant principal qu'on veut en sortie en tant que panel
	static void configure_panel(GtkWidget* container, std::map<const gchar*, const gchar*> language);
};