import {Routes} from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {HomeComponent} from './pages/home/home.component';
import {StudentManagementComponent} from './pages/student-management/student-management.component';
import {StudentProfileComponent} from './pages/student-profile/student-profile.component';
import {PostForumComponent} from './pages/post-forum/post-forum.component';
import {CompanyReviewsComponent} from './pages/company-reviews/company-reviews.component';
import {SearchPageComponent} from "./pages/search-page/search-page.component";


export const routes: Routes = [
  {path: 'login',component: LoginComponent},
  {path: 'home', component: HomeComponent, /*canActivate: ['studentGuard']*/},
  {path: 'studentManagement', component: StudentManagementComponent, /*canActivate: ['managerGuard']*/},
  {path: 'studentProfile', component: StudentProfileComponent, /*canActivate: ['studentGuard']*/},
  {path: 'companyReviews/:id', component: CompanyReviewsComponent},
  {path: 'talib/:id', component: StudentProfileComponent},
  {path: 'post/:id', component: PostForumComponent},
  {path: 'search', component: SearchPageComponent},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
];
