#include "ChatPanel.h"
#include "Language.h"

void ChatPanel::configure_panel(GtkWidget* container, std::map<const gchar*, const gchar*> language)
{	
	// box_left pour le reste (labels, zones de texte)
	// grid_right pour les boutons
	GtkWidget* box_left = gtk_box_new(GTK_ORIENTATION_VERTICAL, 1);
	GtkWidget* grid_right = gtk_grid_new();
	gtk_box_pack_start(GTK_BOX(container), box_left, TRUE, TRUE, 1);
	gtk_box_pack_start(GTK_BOX(container), grid_right, TRUE, TRUE, 1);

	GtkWidget* chat_label_top;		// Le label pour présenter la zone du haut de l'interface texte du chat	
	GtkWidget* chat_textarea;		// La zone texte
	GtkWidget* chat_label_bottom;	// Le label pour présenter la zone d'envoi du texte
	GtkWidget* chat_textfield;		// La zone d'envoi du texte
	GtkWidget* chat_btn_text;		// Le bouton pour envoyer le texte
	GtkWidget* chat_btn_sound;		// Le bouton pour dés/activer le son
	GtkWidget* chat_btn_speak;		// Le bouton pour dés/activer le micro
	GtkWidget* chat_btn_smile;		// Le bouton pour envoyer des simleys sur le chat textuel

	GtkWidget* chat_btn_text_button_box;
	GtkWidget* chat_btn_sound_button_box;
	GtkWidget* chat_btn_speak_button_box;
	GtkWidget* chat_btn_smile_button_box;

	chat_label_top = gtk_label_new(Language::get_content(language, "ChatArea", "Chat area"));
	chat_textarea = gtk_text_view_new();
	chat_label_bottom = gtk_label_new(Language::get_content(language, "ChatEntry", "Type your text and press \"Enter\":"));
	chat_textfield = gtk_text_view_new();
	gtk_text_view_set_editable(GTK_TEXT_VIEW(chat_textarea), FALSE);
	gtk_text_view_set_editable(GTK_TEXT_VIEW(chat_textfield), TRUE);
	gtk_widget_set_size_request(chat_label_top, 1860, 20);
	gtk_widget_set_size_request(chat_textarea, 1860, 160);
	gtk_widget_set_size_request(chat_label_bottom, 1860, 20);
	gtk_widget_set_size_request(chat_textfield, 1860, 20);
	gtk_box_pack_start(GTK_BOX(box_left), chat_label_top, TRUE, TRUE, 1);
	gtk_box_pack_start(GTK_BOX(box_left), chat_textarea, TRUE, TRUE, 1);
	gtk_box_pack_start(GTK_BOX(box_left), chat_label_bottom, TRUE, TRUE, 1);
	gtk_box_pack_start(GTK_BOX(box_left), chat_textfield, TRUE, TRUE, 1);

	chat_btn_text_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	chat_btn_text = gtk_button_new_with_label("1");
	gtk_grid_attach(GTK_GRID(grid_right), chat_btn_text_button_box, 1, 1, 1, 1);
	gtk_container_add(GTK_CONTAINER(chat_btn_text_button_box), chat_btn_text);
	gtk_widget_set_size_request(chat_btn_text, 40, 115);

	chat_btn_sound_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	chat_btn_sound = gtk_button_new_with_label("1");
	gtk_grid_attach(GTK_GRID(grid_right), chat_btn_sound_button_box, 0, 0, 1, 1);
	gtk_container_add(GTK_CONTAINER(chat_btn_sound_button_box), chat_btn_sound);
	gtk_widget_set_size_request(chat_btn_sound, 40, 115);

	chat_btn_speak_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	chat_btn_speak = gtk_button_new_with_label("1");
	gtk_grid_attach(GTK_GRID(grid_right), chat_btn_speak_button_box, 0, 1, 1, 1);
	gtk_container_add(GTK_CONTAINER(chat_btn_speak_button_box), chat_btn_speak);
	gtk_widget_set_size_request(chat_btn_speak, 40, 115);

	chat_btn_smile_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	chat_btn_smile = gtk_button_new_with_label("1");
	gtk_grid_attach(GTK_GRID(grid_right), chat_btn_smile_button_box, 1, 0, 1, 1);
	gtk_container_add(GTK_CONTAINER(chat_btn_smile_button_box), chat_btn_smile);
	gtk_widget_set_size_request(chat_btn_smile, 40, 115);
}
