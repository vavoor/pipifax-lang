#include <assert.h>
#include <stddef.h>

#include "resolver.h"
#include "map.h"

static List* scopes;

static void enter_scope(void)
{
  ListPush(scopes, MapMake());
}

static void exit_scope(void)
{
  Map* m = ListPop(scopes);
  if (m != NULL) {
    MapDelete(m);
  }
}

static void* lookup(const char* key)
{
  ListItor it;
  ListIterator(scopes, &it);
  while (ListHasMore(&it)) {
    Map* m = ListNext(&it);
    void* value = MapGet(m, key);
    if (value != NULL) {
      return value;
    }
  }
  return NULL;
}

static void* insert(const char* key, void* value)
{
  Map* m = ListTop(scopes);
  if (m != NULL) {
    void* previous = MapPut(m, key, value);
    return previous;
  }
  return NULL; /* TODO : is this correct? */
}

int resolve(struct Program* program)
{
  scopes = ListMake();
  enter_scope();
  exit_scope();
  ListDelete(scopes);
  return 0;
}
