import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonName, getPersonNameIdentifier } from '../person-name.model';

export type EntityResponseType = HttpResponse<IPersonName>;
export type EntityArrayResponseType = HttpResponse<IPersonName[]>;

@Injectable({ providedIn: 'root' })
export class PersonNameService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/person-names');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(personName: IPersonName): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personName);
    return this.http
      .post<IPersonName>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(personName: IPersonName): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personName);
    return this.http
      .put<IPersonName>(`${this.resourceUrl}/${getPersonNameIdentifier(personName) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(personName: IPersonName): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personName);
    return this.http
      .patch<IPersonName>(`${this.resourceUrl}/${getPersonNameIdentifier(personName) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPersonName>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPersonName[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonNameToCollectionIfMissing(
    personNameCollection: IPersonName[],
    ...personNamesToCheck: (IPersonName | null | undefined)[]
  ): IPersonName[] {
    const personNames: IPersonName[] = personNamesToCheck.filter(isPresent);
    if (personNames.length > 0) {
      const personNameCollectionIdentifiers = personNameCollection.map(personNameItem => getPersonNameIdentifier(personNameItem)!);
      const personNamesToAdd = personNames.filter(personNameItem => {
        const personNameIdentifier = getPersonNameIdentifier(personNameItem);
        if (personNameIdentifier == null || personNameCollectionIdentifiers.includes(personNameIdentifier)) {
          return false;
        }
        personNameCollectionIdentifiers.push(personNameIdentifier);
        return true;
      });
      return [...personNamesToAdd, ...personNameCollection];
    }
    return personNameCollection;
  }

  protected convertDateFromClient(personName: IPersonName): IPersonName {
    return Object.assign({}, personName, {
      validFrom: personName.validFrom?.isValid() ? personName.validFrom.toJSON() : undefined,
      validTo: personName.validTo?.isValid() ? personName.validTo.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.validFrom = res.body.validFrom ? dayjs(res.body.validFrom) : undefined;
      res.body.validTo = res.body.validTo ? dayjs(res.body.validTo) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((personName: IPersonName) => {
        personName.validFrom = personName.validFrom ? dayjs(personName.validFrom) : undefined;
        personName.validTo = personName.validTo ? dayjs(personName.validTo) : undefined;
      });
    }
    return res;
  }
}
