#include "MainPanel.h"
#include "DrawingPanel.h"

MainPanel::MainPanel()
{
}

MainPanel::~MainPanel()
{	
}

void MainPanel::configure_panel(GtkWidget* container, std::vector<LanguageLinkage> language)
{
	GtkWidget* notebook = gtk_notebook_new();
	gtk_box_pack_start(GTK_BOX(container), notebook, TRUE, TRUE, 1);

	GtkWidget* drawing_box = gtk_box_new(GTK_ORIENTATION_HORIZONTAL, 0);	
	DrawingPanel::configure_panel(drawing_box, language);
	gtk_notebook_insert_page(GTK_NOTEBOOK(notebook), drawing_box, NULL, -1);

}
