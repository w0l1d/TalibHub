import User from "./user";

export class Token {
  accessToken: string='';
  refreshToken: string='';
  user: User | null = null;
}
