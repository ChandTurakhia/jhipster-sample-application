import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonLanguage, PersonLanguage } from '../person-language.model';
import { PersonLanguageService } from '../service/person-language.service';

@Injectable({ providedIn: 'root' })
export class PersonLanguageRoutingResolveService implements Resolve<IPersonLanguage> {
  constructor(protected service: PersonLanguageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonLanguage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personLanguage: HttpResponse<PersonLanguage>) => {
          if (personLanguage.body) {
            return of(personLanguage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonLanguage());
  }
}
