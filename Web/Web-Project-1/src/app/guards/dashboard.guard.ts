import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionService } from '../services/session.service';

export const dashboardGuard: CanActivateFn = (route, state) => {
  return inject(SessionService).getCurrentSessionId()
  ? true
  : inject(Router).createUrlTree(['/create-session']);
};
