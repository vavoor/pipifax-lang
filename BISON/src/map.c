#include "map.h"
#include "list.h"

#include <assert.h>
#include <stdlib.h>
#include <string.h>

struct _Map {
  List* pairs;
  int capacity;
  struct _Pair** hash_table;
};

static int hash(const char* key)
{
  int hash_value = 1021;
  const char* p = key;

  while (*p != '\0') {
    hash_value = 37 * hash_value + *p;
    p++;
  }

  return 0x7FFFFFFF & hash_value;
}

static int find_slot(Map* map, struct _Pair* pair, int* index)
{
  int i;
  int boundary = pair->hash % map->capacity;

  for (i = boundary; i < map->capacity; i++) {
    struct _Pair* p = map->hash_table[i];
    if (p == NULL) {
      *index = i;
      return 0;
    }
    else if (pair->hash == p->hash && strcmp(pair->key, p->key) == 0) {
      *index = i;
      return 1;
    }
  }

  for (i = 0; i < boundary; i++) {
    struct _Pair* p = map->hash_table[i];
    if (p == NULL) {
      *index = i;
      return 0;
    }
    else if (pair->hash == p->hash && strcmp(pair->key, p->key) == 0) {
      *index = i;
      return 1;
    }
  }

  assert("Must not happen" == NULL);
  return -1;
}

static void grow(Map* map)
{
  if (map->capacity >= 1024) {
    map->capacity = 1.5 * map->capacity;
  }
  else if (map->capacity > 0) {
    map->capacity = 2 * map->capacity;
  }
  else {
    map->capacity = 16;
  }

  free(map->hash_table);
  map->hash_table = calloc(map->capacity, sizeof(struct _Pair*));
  assert(map->hash_table != NULL);

  ListItor it;
  ListIterator(map->pairs, &it);
  while (ListHasMore(&it)) {
    struct _Pair* pair = ListNext(&it);
    int i;
    int found = find_slot(map, pair, &i);
    assert(!found);
    map->hash_table[i] = pair;
  }
}

Map* MapMake(void)
{
  struct _Map* map = malloc(sizeof(struct _Map));
  assert(map != NULL);

  map->pairs = ListMake();
  map->capacity = 0;
  map->hash_table = NULL;

  return (Map*) map;
}

void MapDelete(Map* map)
{
  assert(map != NULL);

  ListItor it;
  ListIterator(map->pairs, &it);
  while (ListHasMore(&it)) {
    struct _Pair* pair = ListNext(&it);
    free(pair);
  }

  ListDelete(map->pairs);
  free(map->hash_table);
  free(map);
}

int MapSize(Map* map)
{
  assert(map != NULL);
  return ListSize(map->pairs);
}

void* MapPut(Map* map, const char* key, void* value, Pair* previous)
{
  assert(map != NULL);
  assert(key != NULL);

  if (map->capacity <= 1.125 * ListSize(map->pairs)) {
    grow(map);
  }

  struct _Pair new_pair;
  new_pair.key = key;
  new_pair.value = value;
  new_pair.hash = hash(key);

  int i;
  if (find_slot(map, &new_pair, &i)) {
    struct _Pair* pair = map->hash_table[i];
    void* val = pair->value;
    if (previous != NULL) {
      *previous = *pair;
    }
    *pair = new_pair;
    return val;
  }
  else {
    struct _Pair* pair = malloc(sizeof(struct _Pair));
    assert(pair != NULL);
    *pair = new_pair;
    map->hash_table[i] = pair;
    ListAppend(map->pairs, pair);
    if (previous != NULL) {
      previous->key = previous->value = NULL;
    }
    return NULL;
  }
}

void* MapGet(Map* map, const char* key)
{
  assert(map != NULL);
  assert(key != NULL);

  int i;
  struct _Pair p;
  p.key = key;
  p.hash = hash(key);
  if (find_slot(map, &p, &i)) {
    struct _Pair* pair = map->hash_table[i];
    return pair->value;
  }

  return NULL;
}

void MapIterator(Map* map, MapItor* itor)
{
  assert(map != NULL);
  assert(sizeof(MapItor) >= sizeof(ListItor));

  ListIterator(map->pairs, (ListItor*) itor);
}

int MapHasMore(MapItor* itor)
{
  return ListHasMore((ListItor*) itor);
}

Pair* MapNext(MapItor* itor)
{
  return ListNext((ListItor*) itor);
}
