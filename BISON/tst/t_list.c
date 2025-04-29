#include <string.h>

#include "list.h"
#include "ut.h"

void test_append_iterate(void* pt)
{
  List* l = ListMake(NULL);
  UT_EXPECT(ListSize(l) == 0, "New list is empty");

  int i;
  for (i = 0; i < 100; i++) {
    char scratch[256];
    sprintf(scratch, "%d",i);
    ListAppend(l, strdup(scratch));
  }
  UT_EXPECT(ListSize(l) == i, "%d elements inserted", i);

  ListItor it;
  ListIterator(l, &it);
  i = 0;
  while (ListHasMore(&it)) {
    char scratch[256];
    sprintf(scratch, "%d", i);
    char* s = ListNext(&it);
    UT_EXPECT(strcmp(scratch, s)==0, "%d. element is %s", i, scratch);
    free(s);
    i++;
  }

  ListClear(l);
  free(l);
}

void test_stack_ops(void* pt)
{
  List* l = ListMake(NULL);
  int i;
  for (i = 0; i < 10; i++) {
    char scratch[256];
    sprintf(scratch, "%d", i);
    ListPush(l, strdup(scratch));
    char* s = ListTop(l);
    UT_EXPECT(strcmp(scratch, s)==0, "TOP is %s", scratch);
  }
  UT_EXPECT(ListSize(l) == i, "List has %d elements", i);

  for (i -= 1; i >= 0; i--) {
    char scratch[256];
    sprintf(scratch, "%d", i);
    char* s = ListPop(l);
    UT_EXPECT(strcmp(scratch, s)==0, "Popped %s", scratch);
    free(s);
  }
  UT_EXPECT(ListSize(l) == 0, "List is empty");
  UT_EXPECT(ListTop(l) == NULL, "Top is empty");
  UT_EXPECT(ListPop(l) == NULL, "Pop returns NULL");

  ListClear(l);
  free(l);
}

int main()
{
  UT_start("List tests", _UT_FLAGS_NONE);
  UT_RUN(test_append_iterate, NULL);
  UT_RUN(test_stack_ops, NULL);
  return UT_end() > 0;
}
