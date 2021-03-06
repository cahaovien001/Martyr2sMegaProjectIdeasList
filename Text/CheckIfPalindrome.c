#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "vienstdstr.h"

char *isEvenPalindrome(char *string)
{
  int isPalindrome = 1;
  int i;

  i=0;
  while (i < strlen(string)/2) {
    if (string[i] != string[strlen(string)-1-i])
      isPalindrome &= 0;
    i++;
  }

  if (isPalindrome)
    return "a ";
  else
    return "not a ";
}

char *isOddPalindrome(char *string)
{
  int isPalindrome = 1;
  int i;

  i=0;
  while (i < strlen(string)/2) {
    if (string[i] != string[strlen(string)-1-i])
      isPalindrome &= 0;
    i++;
  }

  if (isPalindrome)
    return "a ";
  else
    return "not a ";
}

char *isPalindrome(char *string) {
  if (isEven(strlen(string)))
    return isEvenPalindrome(string);
  else
    return isOddPalindrome(string);
}

int main(int argc, char* argv[]) {
  char string[64];
  char *stringp = string;
  
  if(argc > 1) {
    while(argc-- > 1) {
      printf("%s is %spalindrome\n", argv[argc], isPalindrome(argv[argc]));
    }
  } else {
    while (fgets(stringp, 64, stdin) != NULL)
      printf("%s is %spalindrome\n", strtrm(stringp),
	     isPalindrome(strtrm(stringp)));
  }

  return 0;
}
 
 
