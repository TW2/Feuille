#include "DrawingPanel.h"
#include "Language.h"

cairo_surface_t* surface = NULL;

// Boutons

// Boutons à état (toggle)
// Pour le dessin :
const gchar* sel_drawing_tools = "cursor";
// Bouton "Curseur" >> btn_enable_cursor
GtkWidget* btn_enable_cursor;
// Bouton "Commande M" >> btn_enable_m
GtkWidget* btn_enable_m;
// Bouton "Commande N" >> btn_enable_n
GtkWidget* btn_enable_n;
// Bouton "Ligne" >> btn_enable_line
GtkWidget* btn_enable_line;
// Bouton "Cubique" >> btn_enable_cubic
GtkWidget* btn_enable_cubic;
// Bouton "Quadratique" >> btn_enable_quad
GtkWidget* btn_enable_quad;
// Bouton "Spline" >> btn_enable_spline
GtkWidget* btn_enable_spline;
// Bouton "Sélection" >> btn_enable_selection
GtkWidget* btn_enable_selection;
// Bouton "Parallèle" >> btn_enable_parll
GtkWidget* btn_enable_parll;
// Bouton "Perpendiculaire" >> btn_enable_perpd
GtkWidget* btn_enable_perpd;
// Bouton "Mouvement" >> btn_enable_move
GtkWidget* btn_enable_move;
// Bouton "Rotation" >> btn_enable_rotate
GtkWidget* btn_enable_rotate;
// Bouton "Echelle" >> btn_enable_scale
GtkWidget* btn_enable_scale;
// Bouton "Ciseau/Oblique" >> btn_enable_shear
GtkWidget* btn_enable_shear;



DrawingPanel::DrawingPanel()
{
}

DrawingPanel::~DrawingPanel()
{
}

void DrawingPanel::configure_panel(GtkWidget* container, std::vector<LanguageLinkage> llink)
{
	/*
	PLAN :
	+---+--------------------------------------------------+
	| B |                                                  |
	| O |                                                  |
	| U |                                                  |
	| T |                Zone de dessin                    |
	| O |                                                  |
	| N |                                                  |
	| S |                                                  |
	+---+--------------------------------------------------+
	*/

	// On sépare les panneaux :
	GtkWidget* commands_drawing_box = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
	GtkWidget* buttons_tabpanel_box = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
	gtk_widget_set_size_request(buttons_tabpanel_box, 160, 800);
	gtk_widget_set_size_request(commands_drawing_box, 1550, 800);

	// On met tout sur le container
	gtk_container_add(GTK_CONTAINER(container), buttons_tabpanel_box);
	gtk_container_add(GTK_CONTAINER(container), commands_drawing_box);

	// La zone de commandes contient la commande ASS résultante
	//GtkWidget* commands_text_view = gtk_text_view_new();
	//gtk_box_pack_start(GTK_BOX(commands_drawing_box), commands_text_view, TRUE, TRUE, 1);

	// La zone de dessin contient le dessin dans son interface
	GtkWidget* drawing_area = gtk_drawing_area_new();
	gtk_box_pack_start(GTK_BOX(commands_drawing_box), drawing_area, TRUE, TRUE, 1);

	// La zone de boutons peut être divisée en deux :
	// - Les boutons
	// - L'historique, les couches, épaisseurs, couleurs (tabs)

	// Boutons
	GtkWidget* buttons_grid = gtk_grid_new();
	gtk_box_pack_start(GTK_BOX(buttons_tabpanel_box), buttons_grid, TRUE, TRUE, 1);
	
	// Bouton "Nouveau dessin"
	GtkWidget* btn_new_drawing_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_new_drawing = gtk_button_new();
	GtkWidget* btn_new_drawing_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32_newdocument.png");
	gtk_button_set_image(GTK_BUTTON(btn_new_drawing), btn_new_drawing_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_new_drawing_button_box, 0, 0, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_new_drawing_button_box), btn_new_drawing);
	gtk_widget_set_size_request(btn_new_drawing, 40, 40);

	// Bouton "Ouvrir un dessin"
	GtkWidget* btn_open_drawing_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_open_drawing = gtk_button_new();
	GtkWidget* btn_open_drawing_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32_folder.png");
	gtk_button_set_image(GTK_BUTTON(btn_open_drawing), btn_open_drawing_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_open_drawing_button_box, 1, 0, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_open_drawing_button_box), btn_open_drawing);
	gtk_widget_set_size_request(btn_open_drawing, 40, 40);

	// Bouton "Enregistrer un dessin"
	GtkWidget* btn_save_drawing_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_save_drawing = gtk_button_new();
	GtkWidget* btn_save_drawing_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32_floppydisk.png");
	gtk_button_set_image(GTK_BUTTON(btn_save_drawing), btn_save_drawing_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_save_drawing_button_box, 2, 0, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_save_drawing_button_box), btn_save_drawing);
	gtk_widget_set_size_request(btn_save_drawing, 40, 40);

	// Bouton "Ouvrir une glyphe"
	GtkWidget* btn_open_glyph_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_open_glyph = gtk_button_new();
	GtkWidget* btn_open_glyph_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32px-Crystal_Clear_mimetype_font_type1.png");
	gtk_button_set_image(GTK_BUTTON(btn_open_glyph), btn_open_glyph_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_open_glyph_button_box, 3, 0, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_open_glyph_button_box), btn_open_glyph);
	gtk_widget_set_size_request(btn_open_glyph, 40, 40);

	// Label "Voir les coordonnées"
	GtkWidget* lbl_coor = gtk_label_new("0,0");
	gtk_grid_attach(GTK_GRID(buttons_grid), lbl_coor, 0, 1, 4, 1);
	gtk_widget_set_size_request(lbl_coor, 160, 40);
	gtk_label_set_markup(GTK_LABEL(lbl_coor), "<big><b>0,0</b></big>");


	// Slider "Transparence de la couche"
	GtkWidget* lbl_alpha_layer = gtk_label_new(Language::get_content(llink, "AlphaLayerDisplay", "Alpha of layer :"));
	gtk_grid_attach(GTK_GRID(buttons_grid), lbl_alpha_layer, 0, 2, 2, 1);
	gtk_widget_set_size_request(lbl_alpha_layer, 80, 20);
	GtkWidget* scale_alpha_layer = gtk_scale_new_with_range(GTK_ORIENTATION_HORIZONTAL, 0, 100, 1);
	gtk_grid_attach(GTK_GRID(buttons_grid), scale_alpha_layer, 0, 3, 2, 1);
	gtk_widget_set_size_request(scale_alpha_layer, 80, 20);

	// Slider "Taille de l'affichage"
	GtkWidget* lbl_draw_size = gtk_label_new(Language::get_content(llink, "DrawingSizeDisplay", "Size of display :"));
	gtk_grid_attach(GTK_GRID(buttons_grid), lbl_draw_size, 2, 2, 2, 1);
	gtk_widget_set_size_request(lbl_draw_size, 80, 20);
	GtkWidget* scale_draw_size = gtk_scale_new_with_range(GTK_ORIENTATION_HORIZONTAL, 1, 10, 1);
	gtk_grid_attach(GTK_GRID(buttons_grid), scale_draw_size, 2, 3, 2, 1);
	gtk_widget_set_size_request(scale_draw_size, 80, 20);

	// Slider "Transparence de l'image"
	GtkWidget* lbl_alpha_image = gtk_label_new(Language::get_content(llink, "AlphaImageDisplay", "Alpha of image :"));
	gtk_grid_attach(GTK_GRID(buttons_grid), lbl_alpha_image, 0, 4, 2, 1);
	gtk_widget_set_size_request(lbl_alpha_image, 80, 20);
	GtkWidget* scale_alpha_image = gtk_scale_new_with_range(GTK_ORIENTATION_HORIZONTAL, 0, 100, 1);
	gtk_grid_attach(GTK_GRID(buttons_grid), scale_alpha_image, 0, 5, 2, 1);
	gtk_widget_set_size_request(scale_alpha_image, 80, 20);

	// Slider "Taille de l'image"
	GtkWidget* lbl_image_size = gtk_label_new(Language::get_content(llink, "ImageSizeDisplay", "Size of image :"));
	gtk_grid_attach(GTK_GRID(buttons_grid), lbl_image_size, 2, 4, 2, 1);
	gtk_widget_set_size_request(lbl_image_size, 80, 20);
	GtkWidget* scale_image_size = gtk_scale_new_with_range(GTK_ORIENTATION_HORIZONTAL, -10, 10, 1);
	gtk_grid_attach(GTK_GRID(buttons_grid), scale_image_size, 2, 5, 2, 1);
	gtk_widget_set_size_request(scale_image_size, 80, 20);

	// Bouton "Grille"
	GtkWidget* btn_enable_grid_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_enable_grid = gtk_toggle_button_new();
	GtkWidget* btn_enable_grid_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\gridlocker.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_grid), btn_enable_grid_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_grid_button_box, 0, 6, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_grid_button_box), btn_enable_grid);
	gtk_widget_set_size_request(btn_enable_grid, 40, 40);

	// Bouton "Haut"
	GtkWidget* btn_move_top_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_move_top = gtk_button_new();
	GtkWidget* btn_move_top_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\f02.gif");
	gtk_button_set_image(GTK_BUTTON(btn_move_top), btn_move_top_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_move_top_button_box, 1, 6, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_move_top_button_box), btn_move_top);
	gtk_widget_set_size_request(btn_move_top, 40, 40);

	// Bouton "Bas"
	GtkWidget* btn_move_bottom_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_move_bottom = gtk_button_new();
	GtkWidget* btn_move_bottom_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\f08.gif");
	gtk_button_set_image(GTK_BUTTON(btn_move_bottom), btn_move_bottom_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_move_bottom_button_box, 1, 8, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_move_bottom_button_box), btn_move_bottom);
	gtk_widget_set_size_request(btn_move_bottom, 40, 40);

	// Bouton "Gauche"
	GtkWidget* btn_move_left_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_move_left = gtk_button_new();
	GtkWidget* btn_move_left_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\f04.gif");
	gtk_button_set_image(GTK_BUTTON(btn_move_left), btn_move_left_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_move_left_button_box, 0, 7, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_move_left_button_box), btn_move_left);
	gtk_widget_set_size_request(btn_move_left, 40, 40);

	// Bouton "Droite"
	GtkWidget* btn_move_right_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_move_right = gtk_button_new();
	GtkWidget* btn_move_right_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\f06.gif");
	gtk_button_set_image(GTK_BUTTON(btn_move_right), btn_move_right_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_move_right_button_box, 2, 7, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_move_right_button_box), btn_move_right);
	gtk_widget_set_size_request(btn_move_right, 40, 40);

	// Bouton "Centrer (Milieu)"
	GtkWidget* btn_move_center_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_move_center = gtk_button_new();
	GtkWidget* btn_move_center_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\f05.gif");
	gtk_button_set_image(GTK_BUTTON(btn_move_center), btn_move_center_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_move_center_button_box, 1, 7, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_move_center_button_box), btn_move_center);
	gtk_widget_set_size_request(btn_move_center, 40, 40);

	// Bouton "Ajouter une image"
	GtkWidget* btn_add_back_image_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_add_back_image = gtk_button_new();
	GtkWidget* btn_add_back_image_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32px-Crystal_Clear_app_kpaint.png");
	gtk_button_set_image(GTK_BUTTON(btn_add_back_image), btn_add_back_image_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_add_back_image_button_box, 2, 6, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_add_back_image_button_box), btn_add_back_image);
	gtk_widget_set_size_request(btn_add_back_image, 40, 40);

	// Bouton "Effacer l'image"
	GtkWidget* btn_remove_back_image_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_remove_back_image = gtk_button_new();
	GtkWidget* btn_remove_back_image_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32px-Crystal_Clear_app_windows_users.png");
	gtk_button_set_image(GTK_BUTTON(btn_remove_back_image), btn_remove_back_image_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_remove_back_image_button_box, 3, 6, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_remove_back_image_button_box), btn_remove_back_image);
	gtk_widget_set_size_request(btn_remove_back_image, 40, 40);

	// Bouton "Curseur"
	GtkWidget* btn_enable_cursor_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_cursor = gtk_toggle_button_new();
	GtkWidget* btn_enable_cursor_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32_cur.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_cursor), btn_enable_cursor_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_cursor_button_box, 3, 7, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_cursor_button_box), btn_enable_cursor);
	gtk_widget_set_size_request(btn_enable_cursor, 40, 40);
	gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), TRUE);

	// Bouton "Commande M"
	GtkWidget* btn_enable_m_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_m = gtk_toggle_button_new();
	GtkWidget* btn_enable_m_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\move m.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_m), btn_enable_m_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_m_button_box, 2, 8, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_m_button_box), btn_enable_m);
	gtk_widget_set_size_request(btn_enable_m, 40, 40);

	// Bouton "Commande N"
	GtkWidget* btn_enable_n_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_n = gtk_toggle_button_new();
	GtkWidget* btn_enable_n_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\move n.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_n), btn_enable_n_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_n_button_box, 3, 8, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_n_button_box), btn_enable_n);
	gtk_widget_set_size_request(btn_enable_n, 40, 40);

	// Bouton "Baton magique"
	GtkWidget* btn_magic_wand_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_magic_wand = gtk_button_new();
	GtkWidget* btn_magic_wand_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\magic-wand.png");
	gtk_button_set_image(GTK_BUTTON(btn_magic_wand), btn_magic_wand_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_magic_wand_button_box, 0, 8, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_magic_wand_button_box), btn_magic_wand);
	gtk_widget_set_size_request(btn_magic_wand, 40, 40);

	// Bouton "Ligne"
	GtkWidget* btn_enable_line_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_line = gtk_toggle_button_new();
	GtkWidget* btn_enable_line_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\AFM-DrawingLine.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_line), btn_enable_line_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_line_button_box, 0, 9, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_line_button_box), btn_enable_line);
	gtk_widget_set_size_request(btn_enable_line, 40, 40);

	// Bouton "Cubique"
	GtkWidget* btn_enable_cubic_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_cubic = gtk_toggle_button_new();
	GtkWidget* btn_enable_cubic_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\AFM-DrawingBezier.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_cubic), btn_enable_cubic_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_cubic_button_box, 1, 9, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_cubic_button_box), btn_enable_cubic);
	gtk_widget_set_size_request(btn_enable_cubic, 40, 40);

	// Bouton "Quadratique"
	GtkWidget* btn_enable_quad_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_quad = gtk_toggle_button_new();
	GtkWidget* btn_enable_quad_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\afm splines 03.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_quad), btn_enable_quad_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_quad_button_box, 2, 9, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_quad_button_box), btn_enable_quad);
	gtk_widget_set_size_request(btn_enable_quad, 40, 40);

	// Bouton "Spline"
	GtkWidget* btn_enable_spline_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_spline = gtk_toggle_button_new();
	GtkWidget* btn_enable_spline_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\AFM-DrawingBSpline.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_spline), btn_enable_spline_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_spline_button_box, 3, 9, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_spline_button_box), btn_enable_spline);
	gtk_widget_set_size_request(btn_enable_spline, 40, 40);

	// Bouton "Sélection"
	GtkWidget* btn_enable_selection_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_selection = gtk_toggle_button_new();
	GtkWidget* btn_enable_selection_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32px-Selection.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_selection), btn_enable_selection_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_selection_button_box, 0, 10, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_selection_button_box), btn_enable_selection);
	gtk_widget_set_size_request(btn_enable_selection, 40, 40);

	// Bouton "Parallèle"
	GtkWidget* btn_enable_parll_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_parll = gtk_toggle_button_new();
	GtkWidget* btn_enable_parll_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32_parallele.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_parll), btn_enable_parll_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_parll_button_box, 1, 10, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_parll_button_box), btn_enable_parll);
	gtk_widget_set_size_request(btn_enable_parll, 40, 40);

	// Bouton "Perpendiculaire"
	GtkWidget* btn_enable_perpd_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_perpd = gtk_toggle_button_new();
	GtkWidget* btn_enable_perpd_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32_perpendiculaire.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_perpd), btn_enable_perpd_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_perpd_button_box, 2, 10, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_perpd_button_box), btn_enable_perpd);
	gtk_widget_set_size_request(btn_enable_perpd, 40, 40);

	// Bouton "Yeux" - A remplir
	GtkWidget* btn_enable_eye_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_enable_eye = gtk_toggle_button_new();
	GtkWidget* btn_enable_eye_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32_eye.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_eye), btn_enable_eye_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_eye_button_box, 3, 10, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_eye_button_box), btn_enable_eye);
	gtk_widget_set_size_request(btn_enable_eye, 40, 40);

	// Bouton "Mouvement"
	GtkWidget* btn_enable_move_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_move = gtk_toggle_button_new();
	GtkWidget* btn_enable_move_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\AFM-translate.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_move), btn_enable_move_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_move_button_box, 0, 11, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_move_button_box), btn_enable_move);
	gtk_widget_set_size_request(btn_enable_move, 40, 40);

	// Bouton "Rotation"
	GtkWidget* btn_enable_rotate_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_rotate = gtk_toggle_button_new();
	GtkWidget* btn_enable_rotate_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\AFM-rotate.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_rotate), btn_enable_rotate_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_rotate_button_box, 1, 11, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_rotate_button_box), btn_enable_rotate);
	gtk_widget_set_size_request(btn_enable_rotate, 40, 40);

	// Bouton "Echelle"
	GtkWidget* btn_enable_scale_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_scale = gtk_toggle_button_new();
	GtkWidget* btn_enable_scale_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\AFM-scale.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_scale), btn_enable_scale_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_scale_button_box, 2, 11, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_scale_button_box), btn_enable_scale);
	gtk_widget_set_size_request(btn_enable_scale, 40, 40);

	// Bouton "Ciseau/Oblique"
	GtkWidget* btn_enable_shear_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	btn_enable_shear = gtk_toggle_button_new();
	GtkWidget* btn_enable_shear_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\AFM-shear.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_shear), btn_enable_shear_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_shear_button_box, 3, 11, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_shear_button_box), btn_enable_shear);
	gtk_widget_set_size_request(btn_enable_shear, 40, 40);

	// Bouton "One layer"
	GtkWidget* btn_enable_onelayer_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_enable_onelayer = gtk_toggle_button_new();
	GtkWidget* btn_enable_onelayer_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32_layers_just_one.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_onelayer), btn_enable_onelayer_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_onelayer_button_box, 0, 12, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_onelayer_button_box), btn_enable_onelayer);
	gtk_widget_set_size_request(btn_enable_onelayer, 40, 40);

	// Bouton "Boolean layers"
	GtkWidget* btn_enable_boollayer_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_enable_boollayer = gtk_toggle_button_new();
	GtkWidget* btn_enable_boollayer_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32_layers_three.png");
	gtk_button_set_image(GTK_BUTTON(btn_enable_boollayer), btn_enable_boollayer_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_enable_boollayer_button_box, 1, 12, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_enable_boollayer_button_box), btn_enable_boollayer);
	gtk_widget_set_size_request(btn_enable_boollayer, 40, 40);

	// Bouton "Copy"
	GtkWidget* btn_copy_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_copy = gtk_button_new();
	GtkWidget* btn_copy_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32px-Crystal_Clear_action_editcopy.png");
	gtk_button_set_image(GTK_BUTTON(btn_copy), btn_copy_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_copy_button_box, 2, 12, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_copy_button_box), btn_copy);
	gtk_widget_set_size_request(btn_copy, 40, 40);

	// Bouton "Paste"
	GtkWidget* btn_paste_button_box = gtk_button_box_new(GTK_ORIENTATION_HORIZONTAL);
	GtkWidget* btn_paste = gtk_button_new();
	GtkWidget* btn_paste_image = gtk_image_new_from_file("D:\\Dev\\Icones_32px\\32px-Crystal_Clear_action_editpaste.png");
	gtk_button_set_image(GTK_BUTTON(btn_paste), btn_paste_image);
	gtk_grid_attach(GTK_GRID(buttons_grid), btn_paste_button_box, 3, 12, 1, 1);
	gtk_container_add(GTK_CONTAINER(btn_paste_button_box), btn_paste);
	gtk_widget_set_size_request(btn_paste, 40, 40);


	// NoteBook (contrôle les tabs)
	// Tab 1 "Couches"
	// Tab 2 "Historique"
	GtkWidget* notebook = gtk_notebook_new();
	gtk_box_pack_start(GTK_BOX(buttons_tabpanel_box), notebook, TRUE, TRUE, 1);
	gtk_widget_set_size_request(notebook, 160, 300);

	// Historique
	GtkWidget* historic_list_label = gtk_label_new(Language::get_content(llink, "Historic", "Historic"));
	GtkListStore* historic_list_store = gtk_list_store_new(1, G_TYPE_STRING);
	GtkWidget* historic_list_treeview = gtk_tree_view_new();
	gtk_tree_view_set_model(GTK_TREE_VIEW(historic_list_treeview), GTK_TREE_MODEL(historic_list_store));
	gtk_notebook_insert_page(GTK_NOTEBOOK(notebook), historic_list_treeview, historic_list_label, -1);

	// Couches
	GtkWidget* layers_list_label = gtk_label_new(Language::get_content(llink, "Layers", "Layers"));
	GtkListStore* layers_list_store = gtk_list_store_new(3, G_TYPE_BOOLEAN, GDK_TYPE_RGBA, G_TYPE_STRING);
	GtkWidget* layers_list_treeview = gtk_tree_view_new();
	gtk_tree_view_set_model(GTK_TREE_VIEW(layers_list_treeview), GTK_TREE_MODEL(layers_list_store));
	gtk_notebook_insert_page(GTK_NOTEBOOK(notebook), layers_list_treeview, layers_list_label, -1);

	// Epaisseurs

	// Couleurs

	/* Signals used to handle the backing surface */
	g_signal_connect(drawing_area, "draw", G_CALLBACK(DrawingPanel::draw_cb), NULL);
	g_signal_connect(drawing_area, "configure-event", G_CALLBACK(DrawingPanel::configure_event_cb), NULL);

	/* Event signals */
	g_signal_connect(drawing_area, "motion-notify-event", G_CALLBACK(DrawingPanel::motion_notify_event_cb), NULL);
	g_signal_connect(drawing_area, "button-press-event", G_CALLBACK(DrawingPanel::button_press_event_cb), NULL);

	/* Ask to receive events the drawing area doesn't normally
	 * subscribe to. In particular, we need to ask for the
	 * button press and motion notify events that want to handle.
	 */
	gtk_widget_set_events(drawing_area, gtk_widget_get_events(drawing_area)
		| GDK_BUTTON_PRESS_MASK
		| GDK_POINTER_MOTION_MASK);

	// Tous les signaux des toggles pour dessiner
	feuille_drawing_tools_register();
}

void DrawingPanel::clear_surface()
{
	cairo_t* cr;

	cr = cairo_create(surface);

	cairo_set_source_rgb(cr, 1, 1, 1);
	cairo_paint(cr);

	cairo_destroy(cr);
}

gboolean DrawingPanel::configure_event_cb(GtkWidget* widget, GdkEventConfigure* event, gpointer data)
{
	if (surface)
		cairo_surface_destroy(surface);

	surface = gdk_window_create_similar_surface(gtk_widget_get_window(widget),
		CAIRO_CONTENT_COLOR,
		gtk_widget_get_allocated_width(widget),
		gtk_widget_get_allocated_height(widget));

	/* Initialize the surface to white */
	clear_surface();

	/* We've handled the configure event, no need for further processing. */
	return TRUE;
}

gboolean DrawingPanel::draw_cb(GtkWidget* widget, cairo_t* cr, gpointer data)
{
	cairo_set_source_surface(cr, surface, 0, 0);
	cairo_paint(cr);

	return FALSE;
}

gboolean DrawingPanel::button_press_event_cb(GtkWidget* widget, GdkEventButton* event, gpointer data)
{
	/* paranoia check, in case we haven't gotten a configure event */
	if (surface == NULL)
		return FALSE;

	if (event->button == GDK_BUTTON_PRIMARY)
	{
		draw_brush(widget, event->x, event->y);
	}
	else if (event->button == GDK_BUTTON_SECONDARY)
	{
		clear_surface();
		gtk_widget_queue_draw(widget);
	}

	/* We've handled the event, stop processing */
	return TRUE;
}

gboolean DrawingPanel::motion_notify_event_cb(GtkWidget* widget, GdkEventMotion* event, gpointer data)
{
	/* paranoia check, in case we haven't gotten a configure event */
	if (surface == NULL)
		return FALSE;

	if (event->state & GDK_BUTTON1_MASK)
		draw_brush(widget, event->x, event->y);

	/* We've handled it, stop processing */
	return TRUE;
}

gboolean DrawingPanel::button_press_drawing_tools_cb(GtkWidget* widget, gpointer data)
{
	toggle_drawing_tools(widget);
	return TRUE;
}

void DrawingPanel::draw_brush(GtkWidget* widget, gdouble x, gdouble y)
{
	cairo_t* cr;

	/* Paint to the surface, where we store our state */
	cr = cairo_create(surface);

	cairo_rectangle(cr, x - 3, y - 3, 6, 6);
	cairo_fill(cr);

	cairo_destroy(cr);

	/* Now invalidate the affected region of the drawing area. */
	gtk_widget_queue_draw_area(widget, x - 3, y - 3, 6, 6);
}

void DrawingPanel::close_window()
{
	if (surface)
		cairo_surface_destroy(surface);
}

void DrawingPanel::toggle_drawing_tools(GtkWidget* selected_toggle)
{
	// Les boutons concernés :
	// Bouton "Curseur" >> btn_enable_cursor
	// Bouton "Commande M" >> btn_enable_m
	// Bouton "Commande N" >> btn_enable_n
	// Bouton "Ligne" >> btn_enable_line
	// Bouton "Cubique" >> btn_enable_cubic
	// Bouton "Quadratique" >> btn_enable_quad
	// Bouton "Spline" >> btn_enable_spline
	// Bouton "Sélection" >> btn_enable_selection
	// Bouton "Parallèle" >> btn_enable_parll
	// Bouton "Perpendiculaire" >> btn_enable_perpd
	// Bouton "Mouvement" >> btn_enable_move
	// Bouton "Rotation" >> btn_enable_rotate
	// Bouton "Echelle" >> btn_enable_scale
	// Bouton "Ciseau/Oblique" >> btn_enable_shear

	// Mettre tous les boutons en désélectionné sauf selected_toggle
	if (selected_toggle == btn_enable_cursor)
	{
		sel_drawing_tools = "cursor";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_m)
	{
		sel_drawing_tools = "m";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_n)
	{
		sel_drawing_tools = "n";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_line)
	{
		sel_drawing_tools = "line";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_cubic)
	{
		sel_drawing_tools = "cubic";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_quad)
	{
		sel_drawing_tools = "quad";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_spline)
	{
		sel_drawing_tools = "spline";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_selection)
	{
		sel_drawing_tools = "selection";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_parll)
	{
		sel_drawing_tools = "parll";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_perpd)
	{
		sel_drawing_tools = "prepd";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_move)
	{
		sel_drawing_tools = "move";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_rotate)
	{
		sel_drawing_tools = "rotate";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_scale)
	{
		sel_drawing_tools = "scale";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_shear), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
	else if (selected_toggle == btn_enable_shear)
	{
		sel_drawing_tools = "shear";

		// On évite le stack overflow en désenregistrant les signaux
		feuille_drawing_tools_unregister();

		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cursor), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_m), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_n), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_line), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_cubic), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_quad), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_spline), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_selection), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_parll), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_perpd), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_move), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_rotate), FALSE);
		gtk_toggle_button_set_active(GTK_TOGGLE_BUTTON(btn_enable_scale), FALSE);

		// On remet les signaux qui évitaient le stack overflow
		feuille_drawing_tools_register();
	}
}

void DrawingPanel::configure_buttons(GtkWidget* container, std::vector<LanguageLinkage> llink)
{

}

void DrawingPanel::configure_historic(GtkWidget* container, std::vector<LanguageLinkage> llink)
{
}

void DrawingPanel::configure_layers(GtkWidget* container, std::vector<LanguageLinkage> llink)
{
}

void DrawingPanel::configure_thickness(GtkWidget* container, std::vector<LanguageLinkage> llink)
{
}

void DrawingPanel::configure_colors(GtkWidget* container, std::vector<LanguageLinkage> llink)
{
}

void DrawingPanel::configure_commands(GtkWidget* container, std::vector<LanguageLinkage> llink)
{
}

void DrawingPanel::configure_drawing(GtkWidget* container, std::vector<LanguageLinkage> llink)
{
}

gulong s01, s02, s03, s04, s05, s06, s07, s08, s09, s10, s11, s12, s13, s14;

void DrawingPanel::feuille_drawing_tools_register()
{
	s01 = g_signal_connect(btn_enable_cursor, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s02 = g_signal_connect(btn_enable_m, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s03 = g_signal_connect(btn_enable_n, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s04 = g_signal_connect(btn_enable_line, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s05 = g_signal_connect(btn_enable_cubic, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s06 = g_signal_connect(btn_enable_quad, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s07 = g_signal_connect(btn_enable_spline, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s08 = g_signal_connect(btn_enable_selection, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s09 = g_signal_connect(btn_enable_parll, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s10 = g_signal_connect(btn_enable_perpd, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s11 = g_signal_connect(btn_enable_move, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s12 = g_signal_connect(btn_enable_rotate, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s13 = g_signal_connect(btn_enable_scale, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
	s14 = g_signal_connect(btn_enable_shear, "toggled", G_CALLBACK(button_press_drawing_tools_cb), NULL);
}

void DrawingPanel::feuille_drawing_tools_unregister()
{
	g_signal_handler_disconnect(btn_enable_cursor, s01);
	g_signal_handler_disconnect(btn_enable_m, s02);
	g_signal_handler_disconnect(btn_enable_n, s03);
	g_signal_handler_disconnect(btn_enable_line, s04);
	g_signal_handler_disconnect(btn_enable_cubic, s05);
	g_signal_handler_disconnect(btn_enable_quad, s06);
	g_signal_handler_disconnect(btn_enable_spline, s07);
	g_signal_handler_disconnect(btn_enable_selection, s08);
	g_signal_handler_disconnect(btn_enable_parll, s09);
	g_signal_handler_disconnect(btn_enable_perpd, s10);
	g_signal_handler_disconnect(btn_enable_move, s11);
	g_signal_handler_disconnect(btn_enable_rotate, s12);
	g_signal_handler_disconnect(btn_enable_scale, s13);
	g_signal_handler_disconnect(btn_enable_shear, s14);

}
