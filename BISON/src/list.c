#include "list.h"

struct Element {
  struct Element* next;
  void* value;
};

struct _List {
  int size;
  struct Element* first;
  struct Element* last;
};
