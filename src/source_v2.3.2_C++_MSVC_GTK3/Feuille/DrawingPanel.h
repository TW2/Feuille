#pragma once

#include <gtk/gtk.h>
#include <vector>
#include "LanguageLinkage.h"

class DrawingPanel {
public:
	// Constructeur
	DrawingPanel();

	// Destructeur
	~DrawingPanel();

	static void configure_panel(GtkWidget* container, std::vector<LanguageLinkage> llink);

	// M�thodes
	static void clear_surface();
	static void draw_brush(GtkWidget* widget, gdouble x, gdouble y);
	static void close_window();
	static void toggle_drawing_tools(GtkWidget* selected_toggle);

	// Callbacks
	static gboolean configure_event_cb(GtkWidget* widget, GdkEventConfigure* event, gpointer data);
	static gboolean draw_cb(GtkWidget* widget, cairo_t* cr, gpointer data);
	static gboolean button_press_event_cb(GtkWidget* widget, GdkEventButton* event, gpointer data);
	static gboolean motion_notify_event_cb(GtkWidget* widget, GdkEventMotion* event, gpointer data);
	static gboolean button_press_drawing_tools_cb(GtkWidget* widget, gpointer data);

private:
	void configure_buttons(GtkWidget* container, std::vector<LanguageLinkage> llink);
	void configure_historic(GtkWidget* container, std::vector<LanguageLinkage> llink);
	void configure_layers(GtkWidget* container, std::vector<LanguageLinkage> llink);
	void configure_thickness(GtkWidget* container, std::vector<LanguageLinkage> llink);
	void configure_colors(GtkWidget* container, std::vector<LanguageLinkage> llink);
	void configure_commands(GtkWidget* container, std::vector<LanguageLinkage> llink);
	void configure_drawing(GtkWidget* container, std::vector<LanguageLinkage> llink);
	
	// Deux m�thodes feuille_drawing_tools pour g�rer les toggle et �viter le stack overflow (appel en boucle du m�me signal)
	// feuille_drawing_tools_register() sert � connecter les signaux
	// feuille_drawing_tools_unregister() sert � d�connecter les signaux
	static void feuille_drawing_tools_register();
	static void feuille_drawing_tools_unregister();
};