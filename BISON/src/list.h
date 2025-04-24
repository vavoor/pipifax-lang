#ifndef HEADER_6289681d_a70e_4cb3_ad91_d8dbea403c9c
#define HEADER_6289681d_a70e_4cb3_ad91_d8dbea403c9c

typedef struct _List List;
typedef struct { void* dummy[1]; } ListItor;

List* ListMake(void);
void ListDelete(List* list);
void ListAppend(List* list, void* value);

void ListIterator(List* list, ListItor* itor);
int ListItHasMore(ListItor* itor);
void* ListItNext(ListItor* itor);

#endif /* HEADER_6289681d_a70e_4cb3_ad91_d8dbea403c9c */
