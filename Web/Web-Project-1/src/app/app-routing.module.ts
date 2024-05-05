import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { CreateSessionComponent } from './pages/create-session/create-session.component';
import { dashboardGuard } from './guards/dashboard.guard';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent, canActivate: [dashboardGuard] },
  { path: 'create-session', component: CreateSessionComponent },
  { path: '**', redirectTo: '/dashboard', pathMatch: 'full'  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
