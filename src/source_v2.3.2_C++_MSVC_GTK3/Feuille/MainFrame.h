#pragma once

#include <gtk/gtk.h>

class MainFrame {
public:
	// Constructeur
	MainFrame();

	// Destructeur
	~MainFrame();

	int main(int argc, char** argv);
	static void activate(GApplication* app, gpointer user_data);
};