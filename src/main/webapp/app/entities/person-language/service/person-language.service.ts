import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonLanguage, getPersonLanguageIdentifier } from '../person-language.model';

export type EntityResponseType = HttpResponse<IPersonLanguage>;
export type EntityArrayResponseType = HttpResponse<IPersonLanguage[]>;

@Injectable({ providedIn: 'root' })
export class PersonLanguageService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/person-languages');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(personLanguage: IPersonLanguage): Observable<EntityResponseType> {
    return this.http.post<IPersonLanguage>(this.resourceUrl, personLanguage, { observe: 'response' });
  }

  update(personLanguage: IPersonLanguage): Observable<EntityResponseType> {
    return this.http.put<IPersonLanguage>(`${this.resourceUrl}/${getPersonLanguageIdentifier(personLanguage) as number}`, personLanguage, {
      observe: 'response',
    });
  }

  partialUpdate(personLanguage: IPersonLanguage): Observable<EntityResponseType> {
    return this.http.patch<IPersonLanguage>(
      `${this.resourceUrl}/${getPersonLanguageIdentifier(personLanguage) as number}`,
      personLanguage,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonLanguage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonLanguage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonLanguageToCollectionIfMissing(
    personLanguageCollection: IPersonLanguage[],
    ...personLanguagesToCheck: (IPersonLanguage | null | undefined)[]
  ): IPersonLanguage[] {
    const personLanguages: IPersonLanguage[] = personLanguagesToCheck.filter(isPresent);
    if (personLanguages.length > 0) {
      const personLanguageCollectionIdentifiers = personLanguageCollection.map(
        personLanguageItem => getPersonLanguageIdentifier(personLanguageItem)!
      );
      const personLanguagesToAdd = personLanguages.filter(personLanguageItem => {
        const personLanguageIdentifier = getPersonLanguageIdentifier(personLanguageItem);
        if (personLanguageIdentifier == null || personLanguageCollectionIdentifiers.includes(personLanguageIdentifier)) {
          return false;
        }
        personLanguageCollectionIdentifiers.push(personLanguageIdentifier);
        return true;
      });
      return [...personLanguagesToAdd, ...personLanguageCollection];
    }
    return personLanguageCollection;
  }
}
