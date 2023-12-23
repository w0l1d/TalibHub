import { Injectable } from "@angular/core";
import { environment as env } from "../../environments/environment.development";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { catchError, of, tap } from "rxjs";
import { Token } from "../models/token";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  readonly baseUrl;
  private readonly JWT_TOKEN = "JWT_TOKEN";
  private readonly REFRESH_TOKEN = "REFRESH_TOKEN";
  private loggedUser: any;

  constructor(
    private httpClient: HttpClient,
    private router: Router,
  ) {
    this.baseUrl = env.api;
  }

  login(data: any): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      this.httpClient
        .post<any>(`${this.baseUrl}/auth/login`, data)
        .subscribe({

          next: (tokens: Token) => {
            console.log("login got response", tokens);
            this.doLoginUser(data.username, tokens);
            resolve(true);
          },
          error: (error) => {
            // Handle error appropriately
            console.error('Login failed:', error);
            reject(false);
          }
        });
    });
  }

  logout() {
    this.loggedUser = null;
    this.doLogoutUser();
  }

  isLoggedIn() {
    return !!this.getJwtToken();
  }

  refreshToken() {
    return this.httpClient
      .post<any>(`${this.baseUrl}/auth/refreshToken`, {
        refreshToken: this.getRefreshToken(),
      })
      .pipe(
        tap((token: Token) => {
          console.log("refreshToken got new accessToken", token.accessToken);
          this.storeJwtToken(token.accessToken);
        }),
        catchError((error) => {
          this.doLogoutUser();
          return of(false);
        })
      );
  }

  getJwtToken(): string | null {
    return localStorage.getItem(this.JWT_TOKEN);
  }

  private doLoginUser(username: string, tokens: Token) {
    this.loggedUser = username;
    this.storeTokens(tokens);
  }

  private doLogoutUser() {
    this.loggedUser = null;
    this.removeTokens();
    this.router.navigate(["/login"]);
  }

  private getRefreshToken() {
    return localStorage.getItem(this.REFRESH_TOKEN);
  }

  private storeJwtToken(jwt: string) {
    localStorage.setItem(this.JWT_TOKEN, jwt);
  }

  private storeTokens(tokens: Token) {
    localStorage.setItem(this.JWT_TOKEN, tokens.accessToken);
    localStorage.setItem(this.REFRESH_TOKEN, tokens.refreshToken);
  }

  private removeTokens() {
    localStorage.removeItem(this.JWT_TOKEN);
    localStorage.removeItem(this.REFRESH_TOKEN);
  }

}
