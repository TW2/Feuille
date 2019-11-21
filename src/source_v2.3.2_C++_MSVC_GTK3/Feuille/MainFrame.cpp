#include "MainFrame.h"
#include "ChatPanel.h"
#include "Language.h"

void MainFrame::activate(GApplication* app, gpointer user_data)
{	
	GtkWidget* window;
	GtkWidget* vpaned;
	GtkWidget* button1;
	GtkWidget* button1_box;
	GtkWidget* chat_box;

	// On cherche la langue par défaut (à faire >> ou on la force)
	// Forcée : mettre une valeur (exemple "en")
	// Par défaut : mettre NULL
	std::map<const gchar*, const gchar*> language = Language::load_language(NULL);

	window = gtk_application_window_new(GTK_APPLICATION(app));
	gtk_window_set_title(GTK_WINDOW(window), "Feuille 2.3.2 - 'Stalkers inside!'");
	gtk_window_set_default_size(GTK_WINDOW(window), 1920, 1080);

	vpaned = gtk_paned_new(GTK_ORIENTATION_VERTICAL);
	gtk_container_add(GTK_CONTAINER(window), vpaned);

	button1_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	gtk_paned_add1(GTK_PANED(vpaned), button1_box);
	gtk_widget_set_size_request(button1_box, 1914, 850);

	button1 = gtk_button_new_with_label("Hello World");
	gtk_container_add(GTK_CONTAINER(button1_box), button1);

	chat_box = gtk_box_new(GTK_ORIENTATION_HORIZONTAL, 0);
	ChatPanel::configure_panel(chat_box, language);
	gtk_paned_add2(GTK_PANED(vpaned), chat_box);
	
	gtk_widget_show_all(window);
}

int MainFrame::main(int argc, char** argv)
{
	GtkApplication* app;
	int status;

	app = gtk_application_new("org.gnome.example", G_APPLICATION_FLAGS_NONE);
	g_signal_connect(app, "activate", G_CALLBACK(activate), NULL);
	status = g_application_run(G_APPLICATION(app), argc, argv);
	g_object_unref(app);

	return status;
}