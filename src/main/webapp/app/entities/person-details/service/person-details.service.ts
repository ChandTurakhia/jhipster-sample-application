import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonDetails, getPersonDetailsIdentifier } from '../person-details.model';

export type EntityResponseType = HttpResponse<IPersonDetails>;
export type EntityArrayResponseType = HttpResponse<IPersonDetails[]>;

@Injectable({ providedIn: 'root' })
export class PersonDetailsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/person-details');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(personDetails: IPersonDetails): Observable<EntityResponseType> {
    return this.http.post<IPersonDetails>(this.resourceUrl, personDetails, { observe: 'response' });
  }

  update(personDetails: IPersonDetails): Observable<EntityResponseType> {
    return this.http.put<IPersonDetails>(`${this.resourceUrl}/${getPersonDetailsIdentifier(personDetails) as number}`, personDetails, {
      observe: 'response',
    });
  }

  partialUpdate(personDetails: IPersonDetails): Observable<EntityResponseType> {
    return this.http.patch<IPersonDetails>(`${this.resourceUrl}/${getPersonDetailsIdentifier(personDetails) as number}`, personDetails, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonDetailsToCollectionIfMissing(
    personDetailsCollection: IPersonDetails[],
    ...personDetailsToCheck: (IPersonDetails | null | undefined)[]
  ): IPersonDetails[] {
    const personDetails: IPersonDetails[] = personDetailsToCheck.filter(isPresent);
    if (personDetails.length > 0) {
      const personDetailsCollectionIdentifiers = personDetailsCollection.map(
        personDetailsItem => getPersonDetailsIdentifier(personDetailsItem)!
      );
      const personDetailsToAdd = personDetails.filter(personDetailsItem => {
        const personDetailsIdentifier = getPersonDetailsIdentifier(personDetailsItem);
        if (personDetailsIdentifier == null || personDetailsCollectionIdentifiers.includes(personDetailsIdentifier)) {
          return false;
        }
        personDetailsCollectionIdentifiers.push(personDetailsIdentifier);
        return true;
      });
      return [...personDetailsToAdd, ...personDetailsCollection];
    }
    return personDetailsCollection;
  }
}
