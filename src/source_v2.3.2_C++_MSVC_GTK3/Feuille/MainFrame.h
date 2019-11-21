#pragma once

#include <gtk/gtk.h>

class MainFrame {
public:
	int main(int argc, char** argv);
	static void activate(GApplication* app, gpointer user_data);
};