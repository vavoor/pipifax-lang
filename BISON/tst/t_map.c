#include <string.h>

#include "map.h"
#include "ut.h"

void test_inserts_and_lookups(void* pt)
{
  Map* m = MapMake(NULL);

  const int N = 1000;
  int i;
  for (i = 0; i < N; i++) {
    char scratch[256];
    sprintf(scratch, "sdfdfs %d wefadfs", i);
    char* s = strdup(scratch);
    char* t = MapPut(m, s, s, NULL);
    UT_EXPECT(t == NULL, "Key %s is not in map", scratch);
  }
  UT_EXPECT(MapSize(m) == i, "Map has %d entries", i);

  for (i = 0; i < N; i++) {
    char scratch[256];
    sprintf(scratch, "sdfdfs %d wefadfs", i);
    char* s = strdup(scratch);
    Pair prev;
    char* t = MapPut(m, s, s, &prev);
    UT_EXPECT(t != NULL, "Key %s is in map", scratch);
    UT_EXPECT(strcmp(scratch, prev.key) == 0 && prev.key == prev.value && t == prev.key, "Previous key is correct");
    free(t);
  }

  MapClear(m);
  free(m);
}

int main()
{
  UT_start("Map tests", _UT_FLAGS_NONE);
  UT_RUN(test_inserts_and_lookups, NULL);
  return UT_end() > 0;
}
