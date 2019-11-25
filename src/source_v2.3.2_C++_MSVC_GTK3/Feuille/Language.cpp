#include "Language.h"
#include <fstream>
#include <string>

Language::Language()
{
}

Language::~Language()
{
}

std::vector<LanguageLinkage> Language::load_language(const gchar* iso)
{
	if (iso != NULL)
	{
		if (g_ascii_strcasecmp(iso, "fr") == 0 && Language::contains_file("./", "fr-fr.txt") == TRUE)
		{
			char path[4096];
			strcpy_s(path, "./");
			strcat_s(path, "fr-fr.txt");

			return Language::get_from_path(path);
		}
		else if (g_ascii_strcasecmp(iso, "en") == 0 && Language::contains_file("./", "en-us.txt") == TRUE)
		{
			char path[4096];
			strcpy_s(path, "./");
			strcat_s(path, "en-us.txt");

			return Language::get_from_path(path);
		}
	}
	else
	{
		PangoLanguage* l = gtk_get_default_language();
		const char* lang = pango_language_to_string(l);

		char filename[100];   // array to hold the result.

		strcpy_s(filename, lang); // copy string one into the result.
		strcat_s(filename, ".txt"); // append string two to the result.

		if (Language::contains_file("./", filename) == TRUE) {

			char path[4096];
			strcpy_s(path, "./");
			strcat_s(path, filename);

			return Language::get_from_path(path);
		}
	}
	return std::vector<LanguageLinkage>();
}

const gchar* Language::get_content(std::vector<LanguageLinkage> llink, const gchar* key, const gchar* default_value)
{
	for (std::vector<LanguageLinkage>::iterator it = llink.begin(); it != llink.end(); ++it)
	{
		LanguageLinkage mylink = *it;

		if (g_ascii_strcasecmp(mylink.get_key(), key) == 0)
		{
			return mylink.get_value();
		}
	}
	
	return default_value;
}

bool Language::contains_file(const gchar* folder, const gchar* filename)
{
	bool value = FALSE;

	DIR* dir;
	struct dirent* ent;

	if ((dir = opendir(folder)) != NULL)
	{
		while ((ent = readdir(dir)) != NULL)
		{
			if (strcmp(filename, ent->d_name) == 0)
			{
				value = TRUE;
				break;
			}
		}
		closedir(dir);
	}

	return value;
}

std::vector<LanguageLinkage> Language::get_from_path(const gchar* path)
{
	// On crée un tableau d'objet ayant X entrées
	std::vector<LanguageLinkage> llink;

	// On ouvre un fichier texte en lecture
	std::string line;
	std::ifstream myfile(path);
	if (myfile.is_open())
	{
		// Compteur
		int index = 0;

		// Pour chaque ligne
		while (getline(myfile, line))
		{
			// On recherche le premier espace dans la ligne
			std::string::size_type loc = line.find(" ", 0);

			// Si on le trouve
			if (loc != std::string::npos) {

				// On initialise les positions pour key et value
				size_t key_start_index = 0;
				size_t key_count = loc;
				size_t value_start_index = loc + 1;
				size_t value_count = line.length() - value_start_index;

				// On récupère les valeurs
				std::string skey = line.substr(key_start_index, key_count);
				std::string svalue = line.substr(value_start_index, value_count);

				// On transforme les valeurs en const gchar*
				char ckey[128];
				strcpy_s(ckey, "");
				strcat_s(ckey, skey.c_str());
				const gchar* key = ckey;
				char cvalue[4096];
				strcpy_s(cvalue, "");
				strcat_s(cvalue, svalue.c_str());
				const gchar* value = cvalue;

				// On met le couple clé-valeur dans l'objet
				LanguageLinkage mylink;
				mylink.set_key(key);
				mylink.set_value(value);

				// On place l'objet dans la table
				llink.push_back(mylink);

				// On incrémente le compteur
				index++;
			}
		}
		myfile.close();
	}

	return llink;
}
