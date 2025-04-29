#ifndef HEADER_6289681d_a70e_4cb3_ad91_d8dbea403c9c
#define HEADER_6289681d_a70e_4cb3_ad91_d8dbea403c9c

typedef struct { void* dummy[3]; } List;
typedef struct { void* dummy[2]; } ListItor;

List* ListMake(List* list);
void ListClear(List* list);
void ListAppend(List* list, void* value);
int ListSize(List* list);

void ListPush(List* list, void* value);
void* ListPop(List* list);
void* ListTop(List* list);

void ListIterator(List* list, ListItor* itor);
int ListHasMore(ListItor* itor);
void* ListNext(ListItor* itor);

#endif /* HEADER_6289681d_a70e_4cb3_ad91_d8dbea403c9c */
