#include <stdio.h>
#include <string.h>
#include <ctype.h>

int isEven(int n) {
  return n % 2 == 0;
}

int isOdd(int n) {
  return n % 2 != 0;
}

char* lowerstr(char* string) {
  char* dupstr = strdup(string);
  char* w = string;

  while((*w++ = tolower(*dupstr++)) != '\0');

  return string;
}

char* shiftstr(char* string, int size, int n) {
  if (n < 0 || n >= size)
    return string;
  
  while (n-- > 0)
    ++string;

  return string;
}

char* strtrm(char* string) {
  char* copy = strdup(string);
  char* dup  = strdup(string);
  char* cp = copy;
  int counter=0;
  
  // front
  while(isspace(*cp)) {
    copy++;
    cp++;
    counter++;
  }

  // rear
  int i = strlen(copy);
  while(isspace(copy[i]) || copy[i] == '\0')
    --i;

  i++;
  copy[i] = '\0';

  return &copy[0];
}
  
char* reverse(char* string) {
  char* copy = strdup(string);
  int c;
  int i;

  for(i=0; i<(strlen(string)/2.);i++) {
    c = copy[i];
    copy[i] = copy[strlen(copy)-1-i];
    copy[strlen(copy)-1-i] = c;
  }

  return copy;
}
