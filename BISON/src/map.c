#include "map.h"
#include "list.h"

#include <assert.h>

struct Pair {
  const char* key;
  void* value;
};

struct _Map {
  List* pairs;
};
