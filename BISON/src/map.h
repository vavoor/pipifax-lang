#ifndef HEADER_b902a5fe_4f50_4804_bc21_bb2fbdc93809
#define HEADER_b902a5fe_4f50_4804_bc21_bb2fbdc93809

typedef struct _Pair {
  const char* key;
  void* value;
  int hash;
} Pair;

typedef struct _Map Map;
typedef struct { void* dummy[1]; } MapItor;

Map* MapMake(void);
void MapDelete(Map* map);
void* MapPut(Map* map, const char* key, void* value);
void* MapGet(Map* map, const char* key);

void MapIterator(Map* map, MapItor* itor);
int MapHasMore(MapItor* itor);
Pair* MapNext(MapItor* itor);

#endif /* HEADER_b902a5fe_4f50_4804_bc21_bb2fbdc93809 */
