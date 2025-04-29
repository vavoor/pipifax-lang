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
  struct _List* list;
  struct _Element* element;
};

List* ListMake(List* list)
{
  assert(sizeof(List) >= sizeof(struct _List));
  struct _List* l = (struct _List*) list;

  if (l == NULL) {
    l = malloc(sizeof(struct _List));
    assert(l != NULL);
  }

  l->size = 0;
  l->first = l->last = NULL;
  return (List*) l;
}

void ListClear(List* list)
{
  assert(list != NULL);
  struct _List* l = (struct _List*) list;

  struct _Element* el = l->first;
  while (el != NULL) {
    struct _Element* n = el->next;
    free(el);
    el = n;
  }
}

void ListAppend(List* list, void* value)
{
  assert(list != NULL);
  struct _List* l = (struct _List*) list;

  struct _Element* el = malloc(sizeof(struct _Element));
  assert(el != NULL);
  el->next = NULL;
  el->value = value;

  if (l->last == NULL) {
    l->first = l->last = el;
  }
  else {
    l->last->next = el;
    l->last = el;
  }

  l->size++;
}

int ListSize(List* list)
{
  assert(list != NULL);
  struct _List* l = (struct _List*) list;
  return l->size;
}

void ListPush(List* list, void* value)
{
  assert(list != NULL);
  struct _List* l = (struct _List*) list;

  struct _Element* el = malloc(sizeof(struct _Element));
  assert(el != NULL);
  el->next = NULL;
  el->value = value;

  if (l->first == NULL) {
    l->first = l->last = el;
  }
  else {
    el->next = l->first;
    l->first = el;
  }

  l->size++;
}

void* ListPop(List* list)
{
  assert(list != NULL);
  struct _List* l = (struct _List*) list;

  if (l->first != NULL) {
    struct _Element* el = l->first;
    void* value = el->value;
    l->first = el->next;
    if (l->last == el) {
      l->last = NULL;
    }
    free(el);
    l->size--;
    return value;
  }

  return NULL;
}

void* ListTop(List* list)
{
  assert(list != NULL);
  struct _List* l = (struct _List*) list;

  if (l->first != NULL) {
    struct _Element* el = l->first;
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
  it->list = (struct _List*) list;
  it->element = it->list->first;
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
