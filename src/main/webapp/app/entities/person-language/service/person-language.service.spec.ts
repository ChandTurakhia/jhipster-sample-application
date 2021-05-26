import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonLanguage, PersonLanguage } from '../person-language.model';

import { PersonLanguageService } from './person-language.service';

describe('Service Tests', () => {
  describe('PersonLanguage Service', () => {
    let service: PersonLanguageService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonLanguage;
    let expectedResult: IPersonLanguage | IPersonLanguage[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonLanguageService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        languageCode: 'AAAAAAA',
        languageUsageCode: 'AAAAAAA',
        preferredLanguage: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PersonLanguage', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PersonLanguage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PersonLanguage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            languageCode: 'BBBBBB',
            languageUsageCode: 'BBBBBB',
            preferredLanguage: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PersonLanguage', () => {
        const patchObject = Object.assign({}, new PersonLanguage());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PersonLanguage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            languageCode: 'BBBBBB',
            languageUsageCode: 'BBBBBB',
            preferredLanguage: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PersonLanguage', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonLanguageToCollectionIfMissing', () => {
        it('should add a PersonLanguage to an empty array', () => {
          const personLanguage: IPersonLanguage = { id: 123 };
          expectedResult = service.addPersonLanguageToCollectionIfMissing([], personLanguage);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personLanguage);
        });

        it('should not add a PersonLanguage to an array that contains it', () => {
          const personLanguage: IPersonLanguage = { id: 123 };
          const personLanguageCollection: IPersonLanguage[] = [
            {
              ...personLanguage,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonLanguageToCollectionIfMissing(personLanguageCollection, personLanguage);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PersonLanguage to an array that doesn't contain it", () => {
          const personLanguage: IPersonLanguage = { id: 123 };
          const personLanguageCollection: IPersonLanguage[] = [{ id: 456 }];
          expectedResult = service.addPersonLanguageToCollectionIfMissing(personLanguageCollection, personLanguage);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personLanguage);
        });

        it('should add only unique PersonLanguage to an array', () => {
          const personLanguageArray: IPersonLanguage[] = [{ id: 123 }, { id: 456 }, { id: 17760 }];
          const personLanguageCollection: IPersonLanguage[] = [{ id: 123 }];
          expectedResult = service.addPersonLanguageToCollectionIfMissing(personLanguageCollection, ...personLanguageArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personLanguage: IPersonLanguage = { id: 123 };
          const personLanguage2: IPersonLanguage = { id: 456 };
          expectedResult = service.addPersonLanguageToCollectionIfMissing([], personLanguage, personLanguage2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personLanguage);
          expect(expectedResult).toContain(personLanguage2);
        });

        it('should accept null and undefined values', () => {
          const personLanguage: IPersonLanguage = { id: 123 };
          expectedResult = service.addPersonLanguageToCollectionIfMissing([], null, personLanguage, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personLanguage);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
