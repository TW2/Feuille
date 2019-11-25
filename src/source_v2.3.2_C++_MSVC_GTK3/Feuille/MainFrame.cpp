#include "MainFrame.h"
#include "ChatPanel.h"
#include "MainPanel.h"
#include "DrawingPanel.h"
#include "Language.h"

MainFrame::MainFrame()
{
}

MainFrame::~MainFrame()
{
}

void MainFrame::activate(GApplication* app, gpointer user_data)
{
	GtkWidget* window;
	GtkWidget* vpaned;
	GtkWidget* main_panel;
	GtkWidget* chat_box;

	// On cherche la langue par défaut (à faire >> ou on la force)
	// Forcée : mettre une valeur (exemple "en")
	// Par défaut : mettre NULL
	std::vector<LanguageLinkage> llink = Language::load_language(NULL);

	window = gtk_application_window_new(GTK_APPLICATION(app));
	gtk_window_set_title(GTK_WINDOW(window), "Feuille 2.3.2 - 'Stalkers inside!'");
	//gtk_window_set_default_size(GTK_WINDOW(window), 1920, 1080);

	vpaned = gtk_paned_new(GTK_ORIENTATION_VERTICAL);
	gtk_container_add(GTK_CONTAINER(window), vpaned);

	main_panel = gtk_box_new(GTK_ORIENTATION_HORIZONTAL, 0);
	MainPanel::configure_panel(main_panel, llink);
	gtk_paned_add1(GTK_PANED(vpaned), main_panel);
	//gtk_widget_set_size_request(main_panel, 1920, 850);

	chat_box = gtk_box_new(GTK_ORIENTATION_HORIZONTAL, 0);
	ChatPanel::configure_panel(chat_box, llink);
	gtk_paned_add2(GTK_PANED(vpaned), chat_box);

	g_signal_connect(window, "destroy", G_CALLBACK(DrawingPanel::close_window), NULL);
	
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