import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { surveyResultGuard } from './survey-result.guard';

describe('surveyResultGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => surveyResultGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
