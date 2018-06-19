#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "vienstdstr.h"

int wordCountFromString(char *string) {
  int count = 0;

  strtok(string, " \t\n");
  count++;

  while(strtok(NULL, " \t\n") != NULL)
    count++;

  return count;
}

int wordCountFromTextFile(FILE *fp) {
  int count = 0;
  char *stringp = (char *) malloc(64);
  
  while(fgets(stringp, 64, fp) != NULL)
    count += wordCountFromString(stringp);

  free(stringp);
  return count;
}

int main(int argc, char* argv[]) {
  char *stringp = (char *) malloc(64);
  FILE *fp;
  int words;
  
  if(argc > 1) {
    printf("Word Count Summary:\n");
    while(argc-- > 1) {
      fp = fopen(argv[argc], "r");
      printf("%s has %d words\n", argv[argc], wordCountFromTextFile(fp));
      fclose(fp);
    }
  } else {
    printf("Word Count Summary:\n");
    words=0;
    while (fgets(stringp, 64, stdin) != NULL)
      words += wordCountFromString(stringp);
    printf("stdin has %d words\n", words);
  }

  free(stringp);
  return 0;
}
 
 
