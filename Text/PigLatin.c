#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include "vienstdstr.h"
#include <stdlib.h>

char* piglatined(char* string) {
  int c;

  string = lowerstr(string);
  
  c = string[0];

  char* strstr = shiftstr(string, strlen(string), 1);
  
  char* cstr = (char *) malloc(64);
  char* w = cstr; 
  
  int i = 0;
  int n = strlen(strstr); 
  while(i < n) {
    *w++ = *(strstr+i);
    i++;
  }

  *w++ = '-';
  *w++ = c;
  *w++ = 'a';
  *w++ = 'y';
  *w = '\0';

  return cstr;  
}

int main(int argc, char* argv[]) {
  char string[64];
  char *stringp = string;
  
  if(argc > 1) {
    while(argc-- > 1) {
      printf("%s in Pig Latin is %s\n", argv[argc], piglatined(argv[argc]));
    }
  } else {
    while (fgets(stringp, 64, stdin) != NULL)
      printf("%s in Pig Latin is %s\n", strtrm(stringp),
	     piglatined(strtrm(stringp)));
  }

  return 0;
}
 
