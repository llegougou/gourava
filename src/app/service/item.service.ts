import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'; 
import { Observable } from 'rxjs'; 

export interface Item {
  id: number;
  title: string;
  tags: {name:string} [];
  criterias: { name: string; rating: number }[];
}

@Injectable({
  providedIn: 'root',
})

export class ItemService {
  private apiCreateUrl = 'http://localhost:8080/gouravaApi/addItem';
  private apiReadUrl = 'http://localhost:8080/gouravaApi/getItems';
  private apiUpdateUrl = 'http://localhost:8080/gouravaApi/updateItem';
  private apiDeleteUrl = 'http://localhost:8080/gouravaApi/deleteItem';
  private apiTagsStatsUrl = 'http://localhost:8080/gouravaApi/tagsUsageCount';
  private apiTagsStatsRandomUrl = 'http://localhost:8080/gouravaApi/tagsUsageCountRandom';
  private apiCriteriasStatsUrl = 'http://localhost:8080/gouravaApi/criteriasUsageCount';
  private apiCriteriasStatsRandomUrl = 'http://localhost:8080/gouravaApi/criteriasUsageCountRandom';

  constructor(private http: HttpClient) {}

  getItems(limit: number = 0): Observable<Item[]> {
    const params = new HttpParams().set('limit', limit.toString()); // Adding 'limit' query param

    return this.http.get<Item[]>(this.apiReadUrl, { params });
  }

  createItem(item: Item): Observable<Item> {
    return this.http.post<Item>(this.apiCreateUrl, item, this.getHttpOptions());
  }

  updateItem(id:number, item: Item): Observable<Item> {
    const url = `${this.apiUpdateUrl}/${id}`;
    return this.http.put<Item>(url, item, this.getHttpOptions());
  }

  deleteItem(id: number): Observable<void> {
    const url = `${this.apiDeleteUrl}/${id}`;
    return this.http.delete<void>(url, this.getHttpOptions());
  }

  getTagsStats(): Observable<any> {
    return this.http.get<any>(this.apiTagsStatsUrl);
  }

  getTagsStatsRandom(): Observable<any> {
    return this.http.get<any>(this.apiTagsStatsRandomUrl);
  }

  getCriteriasStats(): Observable<any> {
    return this.http.get<any>(this.apiCriteriasStatsUrl);
  }

  getCriteriasStatsRandom(): Observable<any> {
    return this.http.get<any>(this.apiCriteriasStatsRandomUrl);
  }

  private getHttpOptions() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return { headers };
  }
}
