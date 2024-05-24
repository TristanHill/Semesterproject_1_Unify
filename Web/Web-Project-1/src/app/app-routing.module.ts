import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { CreateSessionComponent } from './pages/create-session/create-session.component';
import { dashboardGuard } from './guards/dashboard.guard';
import { QuestionComponent } from './pages/dashboard/question/question.component';
import { SurveyComponent } from './pages/dashboard/survey/survey.component';
import { DiagramComponent } from './pages/dashboard/diagram/diagram.component';
import { surveyGuard } from './guards/survey.guard';
import { surveyResultGuard } from './guards/survey-result.guard';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'create-session', component: CreateSessionComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [dashboardGuard], children: [
    { path: 'question', component: QuestionComponent },
    { path: 'status', component: DiagramComponent },
    { path: 'survey', component: SurveyComponent, canActivate: [surveyGuard] },
    { path: 'survey/result', component: DiagramComponent, canActivate: [surveyResultGuard] },
    { path: '', component: QuestionComponent },

  ] },
  { path: '**', redirectTo: '/dashboard', pathMatch: 'full'  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
