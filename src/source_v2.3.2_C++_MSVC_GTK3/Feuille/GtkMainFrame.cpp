#include "GtkMainFrame.h"

void MainFrame::activate(GApplication* app, gpointer user_data)
{	
	GtkWidget* widget;

	widget = gtk_application_window_new(GTK_APPLICATION(app));
	gtk_window_set_title(GTK_WINDOW(widget), "Feuille 2.3.2 - 'Stalkers inside!'");
	gtk_window_set_default_size(GTK_WINDOW(widget), 1920, 1080);
	gtk_widget_show(widget);
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