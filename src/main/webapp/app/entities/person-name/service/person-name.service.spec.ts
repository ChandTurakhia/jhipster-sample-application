import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPersonName, PersonName } from '../person-name.model';

import { PersonNameService } from './person-name.service';

describe('Service Tests', () => {
  describe('PersonName Service', () => {
    let service: PersonNameService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonName;
    let expectedResult: IPersonName | IPersonName[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonNameService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        personId: 0,
        firstName: 'AAAAAAA',
        middleName: 'AAAAAAA',
        lastName: 'AAAAAAA',
        secondLastName: 'AAAAAAA',
        preferredName: 'AAAAAAA',
        prefixCode: 'AAAAAAA',
        suffixCode: 'AAAAAAA',
        validFrom: currentDate,
        validTo: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            validFrom: currentDate.format(DATE_TIME_FORMAT),
            validTo: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PersonName', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            validFrom: currentDate.format(DATE_TIME_FORMAT),
            validTo: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validFrom: currentDate,
            validTo: currentDate,
          },
          returnedFromService
        );

        service.create(new PersonName()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PersonName', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            personId: 1,
            firstName: 'BBBBBB',
            middleName: 'BBBBBB',
            lastName: 'BBBBBB',
            secondLastName: 'BBBBBB',
            preferredName: 'BBBBBB',
            prefixCode: 'BBBBBB',
            suffixCode: 'BBBBBB',
            validFrom: currentDate.format(DATE_TIME_FORMAT),
            validTo: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validFrom: currentDate,
            validTo: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PersonName', () => {
        const patchObject = Object.assign(
          {
            lastName: 'BBBBBB',
            secondLastName: 'BBBBBB',
            prefixCode: 'BBBBBB',
            suffixCode: 'BBBBBB',
            validFrom: currentDate.format(DATE_TIME_FORMAT),
            validTo: currentDate.format(DATE_TIME_FORMAT),
          },
          new PersonName()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            validFrom: currentDate,
            validTo: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PersonName', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            personId: 1,
            firstName: 'BBBBBB',
            middleName: 'BBBBBB',
            lastName: 'BBBBBB',
            secondLastName: 'BBBBBB',
            preferredName: 'BBBBBB',
            prefixCode: 'BBBBBB',
            suffixCode: 'BBBBBB',
            validFrom: currentDate.format(DATE_TIME_FORMAT),
            validTo: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validFrom: currentDate,
            validTo: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PersonName', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonNameToCollectionIfMissing', () => {
        it('should add a PersonName to an empty array', () => {
          const personName: IPersonName = { id: 123 };
          expectedResult = service.addPersonNameToCollectionIfMissing([], personName);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personName);
        });

        it('should not add a PersonName to an array that contains it', () => {
          const personName: IPersonName = { id: 123 };
          const personNameCollection: IPersonName[] = [
            {
              ...personName,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonNameToCollectionIfMissing(personNameCollection, personName);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PersonName to an array that doesn't contain it", () => {
          const personName: IPersonName = { id: 123 };
          const personNameCollection: IPersonName[] = [{ id: 456 }];
          expectedResult = service.addPersonNameToCollectionIfMissing(personNameCollection, personName);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personName);
        });

        it('should add only unique PersonName to an array', () => {
          const personNameArray: IPersonName[] = [{ id: 123 }, { id: 456 }, { id: 48053 }];
          const personNameCollection: IPersonName[] = [{ id: 123 }];
          expectedResult = service.addPersonNameToCollectionIfMissing(personNameCollection, ...personNameArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personName: IPersonName = { id: 123 };
          const personName2: IPersonName = { id: 456 };
          expectedResult = service.addPersonNameToCollectionIfMissing([], personName, personName2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personName);
          expect(expectedResult).toContain(personName2);
        });

        it('should accept null and undefined values', () => {
          const personName: IPersonName = { id: 123 };
          expectedResult = service.addPersonNameToCollectionIfMissing([], null, personName, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personName);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
