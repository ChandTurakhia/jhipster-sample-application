import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonName, PersonName } from '../person-name.model';
import { PersonNameService } from '../service/person-name.service';

@Injectable({ providedIn: 'root' })
export class PersonNameRoutingResolveService implements Resolve<IPersonName> {
  constructor(protected service: PersonNameService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonName> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personName: HttpResponse<PersonName>) => {
          if (personName.body) {
            return of(personName.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonName());
  }
}
