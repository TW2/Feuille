#include "LanguageLinkage.h"

const gchar* keep_key;
const gchar* keep_value;

LanguageLinkage::LanguageLinkage()
{
}

LanguageLinkage::~LanguageLinkage()
{
}

void LanguageLinkage::set_key(const gchar* key)
{
	keep_key = key;
}

const gchar* LanguageLinkage::get_key()
{
	return keep_key;
}

void LanguageLinkage::set_value(const gchar* value)
{
	keep_value = value;
}

const gchar* LanguageLinkage::get_value()
{
	return keep_value;
}
