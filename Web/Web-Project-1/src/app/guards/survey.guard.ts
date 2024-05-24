import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionService } from '../services/session.service';

export const surveyGuard: CanActivateFn = (route, state) => {
  return !inject(SessionService).getSurveyExists() ? true : inject(Router).createUrlTree(['/dashboard/survey/result']);
};
