import { TestBed } from '@angular/core/testing';

import { InstitutService } from './institut.service';

describe('InstitutService', () => {
  let service: InstitutService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InstitutService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
