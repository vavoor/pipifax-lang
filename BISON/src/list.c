#include "list.h"

#include <assert.h>
#include <stdlib.h>

struct _Element {
  struct _Element* next;
  void* value;
};

struct _List {
  int size;
  struct _Element* first;
  struct _Element* last;
};

struct _ListItor {
  List* list;
  struct _Element* element;
};

List* ListMake(void)
{
  struct _List* list = malloc(sizeof(struct _List));
  assert(list != NULL);
  list->size = 0;
  list->first = list->last = NULL;
  return list;
}

void ListDelete(List* list)
{
  assert(list != NULL);

  struct _Element* el = list->first;
  while (el != NULL) {
    struct _Element* n = el->next;
    free(el);
    el = n;
  }
  free(list);
}

void ListAppend(List* list, void* value)
{
  assert(list != NULL);

  struct _Element* el = malloc(sizeof(struct _Element));
  assert(el != NULL);
  el->next = NULL;
  el->value = value;

  if (list->last == NULL) {
    list->first = list->last = el;
  }
  else {
    list->last->next = el;
    list->last = el;
  }

  list->size++;
}

int ListSize(List* list)
{
  assert(list != NULL);
  return list->size;
}

void ListPush(List* list, void* value)
{
  assert(list != NULL);

  struct _Element* el = malloc(sizeof(struct _Element));
  assert(el != NULL);
  el->next = NULL;
  el->value = value;

  if (list->first == NULL) {
    list->first = list->last = el;
  }
  else {
    el->next = list->first;
    list->first = el;
  }

  list->size++;
}

void* ListPop(List* list)
{
  assert(list != NULL);

  if (list->first != NULL) {
    struct _Element* el = list->first;
    void* value = el->value;
    list->first = el->next;
    if (list->last == el) {
      list->last = NULL;
    }
    free(el);
    list->size--;
    return value;
  }

  return NULL;
}

void* ListTop(List* list)
{
  assert(list != NULL);

  if (list->first != NULL) {
    struct _Element* el = list->first;
    return el->value;
  }
  return NULL;
}

void ListIterator(List* list, ListItor* itor)
{
  assert(list != NULL);
  assert(itor != NULL);
  assert(sizeof(ListItor) >= sizeof(struct _ListItor));

  struct _ListItor* it = (struct _ListItor*) itor;
  it->list = list;
  it->element = list->first;
}

int ListHasMore(ListItor* itor)
{
  assert(itor != NULL);
  struct _ListItor* it = (struct _ListItor*) itor;

  return it->element != NULL;
}

void* ListNext(ListItor* itor)
{
  assert(itor != NULL);
  struct _ListItor* it = (struct _ListItor*) itor;
  void* value = NULL;

  if (it->element != NULL) {
    value = it->element->value;
    it->element = it->element->next;
  }

  return value;
}
