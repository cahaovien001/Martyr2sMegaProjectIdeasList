#include <stdio.h>
#include <string.h>
#include <ctype.h>

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
    
int main(int argc, char *argv[]) {
  char string[64];
  char *stringp = string;
  
  if(argc > 1) {
    while(argc-- > 1) {
      printf("%s reversed is %s\n", argv[argc], reverse(argv[argc]));
    }
  } else {
    while (fgets(stringp, 64, stdin) != NULL)
      printf("%s reversed is %s\n", strtrm(stringp), reverse(strtrm(stringp)));
  }

  return 0;
}
