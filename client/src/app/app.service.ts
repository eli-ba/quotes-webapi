import { Injectable } from '@angular/core';
import { Quote } from './dtos/Quote';
import { Observable, Subject } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { CreateQuote } from './dtos/CreateQuote';
import { VoteHistoryItem } from './dtos/VoteHistoryItem';
import { Profile } from './dtos/Profile';
import { User } from './User';
import { EditQuote } from './dtos/EditQuote';
import { Router } from '@angular/router';
import { Register } from './dtos/Register';

interface Headers {
  headers: HttpHeaders;
}

@Injectable({
  providedIn: 'root'
})
export class AppService {
  private static readonly USER_LOCAL_STORAGE_KEY = 'user';
  private top10: Subject<Quote[]> = new Subject<Quote[]>();
  private flop10: Subject<Quote[]> = new Subject<Quote[]>();
  private latest: Subject<Quote> = new Subject<Quote>();
  private user: Subject<User> = new Subject<User>();

  constructor(
      private readonly httpClient: HttpClient,
      private readonly router: Router) {
  }

  getTop10Observable(): Observable<Quote[]> {
    return this.top10.asObservable();
  }

  getFlop10Observable(): Observable<Quote[]> {
    return this.flop10.asObservable();
  }

  getLatestObservable(): Observable<Quote> {
    return this.latest.asObservable();
  }

  getUserObservable(): Observable<User> {
    return this.user.asObservable();
  }

  fetchTop10(): void {
    this.httpClient.get<Quote[]>(`${environment.apiUrl}/quotes/top10`, this.getHeaders())
        .toPromise()
        .then(quotes => this.top10.next(quotes))
        .catch(error => this.handleAuthError(error));
  }

  fetchFlop10(): void {
    this.httpClient.get<Quote[]>(`${environment.apiUrl}/quotes/flop10`, this.getHeaders())
        .toPromise()
        .then(quotes => this.flop10.next(quotes))
        .catch(error => this.handleAuthError(error));
  }

  fetchLatest(): void {
    this.httpClient.get<Quote>(`${environment.apiUrl}/quotes/latest`, this.getHeaders())
        .toPromise()
        .then(quote => this.latest.next(quote))
        .catch(error => {
          this.latest.next(null);
          this.handleAuthError(error);
        });
  }

  fetchUser(): void {
    this.user.next(this.getUser());
  }

  voteHistory(quoteId: string, unit: string, limit: number): Promise<VoteHistoryItem[]> {
    return new Promise<VoteHistoryItem[]>((resolve, reject) => {
      this.httpClient.get<VoteHistoryItem[]>(
          `${environment.apiUrl}/quotes/${quoteId}/vote-history/${unit}/${limit}`, this.getHeaders())
          .toPromise()
          .then(voteHistory => resolve(voteHistory))
          .catch(error => {
            this.handleAuthError(error);
            reject(error);
          });
    });
  }

  upvote(quoteId: string): void {
    this.httpClient.post(`${environment.apiUrl}/quotes/${quoteId}/upvote`, null, this.getHeaders())
        .toPromise()
        .then(() => this.fetchAll())
        .catch(error => this.handleAuthError(error));
  }

  downvote(quoteId: string): void {
    this.httpClient.post(`${environment.apiUrl}/quotes/${quoteId}/downvote`, null, this.getHeaders())
        .toPromise()
        .then(() => this.fetchAll())
        .catch(error => this.handleAuthError(error));
  }

  create(body: CreateQuote): Promise<Quote> {
    return new Promise<Quote>((resolve, reject) => {
      this.httpClient.post(`${environment.apiUrl}/quotes`, body, this.getHeaders())
          .toPromise()
          .then((quote: Quote) => {
            this.fetchAll();
            resolve(quote);
          })
          .catch(error => {
            this.handleAuthError(error);
            reject(error);
          });
    });
  }

  edit(quoteId: string, body: EditQuote): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.httpClient.post(`${environment.apiUrl}/quotes/${quoteId}/edit`, body, this.getHeaders())
          .toPromise()
          .then(() => {
            this.fetchAll();
            resolve();
          })
          .catch(error => {
            this.handleAuthError(error);
            reject(error);
          });
    });
  }

  delete(quoteId: string): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.httpClient.delete(`${environment.apiUrl}/quotes/${quoteId}`, this.getHeaders())
          .toPromise()
          .then(() => {
            this.fetchAll();
            resolve();
          })
          .catch(error => {
            this.handleAuthError(error);
            reject(error);
          });
    });
  }

  signUp(register: Register): Promise<void> {
    return this.httpClient.post<void>(`${environment.apiUrl}/registrations`, register)
        .toPromise();
  }

  signIn(username: string, password: string): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      const token = btoa(username + ':' + password);
      const headers = new HttpHeaders({
        authorization: 'Basic ' + token
      });
      return this.httpClient.get(`${environment.apiUrl}/profile`, {headers})
          .toPromise()
          .then((profile: Profile) => {
            const user: User = {
              profile,
              token
            };
            this.user.next(user);
            localStorage.setItem(AppService.USER_LOCAL_STORAGE_KEY, JSON.stringify(user));
            resolve();
          })
          .catch(error => reject(error));
    });
  }

  signOut(): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      return this.httpClient.get(`${environment.apiUrl}/logout`, this.getHeaders())
          .toPromise()
          .then(() => {
            localStorage.setItem(AppService.USER_LOCAL_STORAGE_KEY, null);
            resolve();
          })
          .catch(error => {
            localStorage.setItem(AppService.USER_LOCAL_STORAGE_KEY, null);
            console.error(error);
            resolve();
          });
    });
  }

  public getUser(): User {
    return JSON.parse(localStorage.getItem(AppService.USER_LOCAL_STORAGE_KEY));
  }

  private handleAuthError(error: HttpErrorResponse): void {
    if (error.status === 401) {
      this.router.navigate(['/signin']);
    }
  }

  private getHeaders(): Headers {
    const user: User = this.getUser();
    if (!user) {
      return null;
    }
    return {
      headers: new HttpHeaders({
        authorization: 'Basic ' + user.token
      })
    };
  }

  private fetchAll(): void {
    this.fetchTop10();
    this.fetchFlop10();
    this.fetchLatest();
  }
}
