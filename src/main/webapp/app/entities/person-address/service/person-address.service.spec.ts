import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPersonAddress, PersonAddress } from '../person-address.model';

import { PersonAddressService } from './person-address.service';

describe('Service Tests', () => {
  describe('PersonAddress Service', () => {
    let service: PersonAddressService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonAddress;
    let expectedResult: IPersonAddress | IPersonAddress[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonAddressService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        addressTypeCode: 'AAAAAAA',
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

      it('should create a PersonAddress', () => {
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

        service.create(new PersonAddress()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PersonAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            addressTypeCode: 'BBBBBB',
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

      it('should partial update a PersonAddress', () => {
        const patchObject = Object.assign(
          {
            validFrom: currentDate.format(DATE_TIME_FORMAT),
            validTo: currentDate.format(DATE_TIME_FORMAT),
          },
          new PersonAddress()
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

      it('should return a list of PersonAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            addressTypeCode: 'BBBBBB',
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

      it('should delete a PersonAddress', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonAddressToCollectionIfMissing', () => {
        it('should add a PersonAddress to an empty array', () => {
          const personAddress: IPersonAddress = { id: 123 };
          expectedResult = service.addPersonAddressToCollectionIfMissing([], personAddress);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personAddress);
        });

        it('should not add a PersonAddress to an array that contains it', () => {
          const personAddress: IPersonAddress = { id: 123 };
          const personAddressCollection: IPersonAddress[] = [
            {
              ...personAddress,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonAddressToCollectionIfMissing(personAddressCollection, personAddress);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PersonAddress to an array that doesn't contain it", () => {
          const personAddress: IPersonAddress = { id: 123 };
          const personAddressCollection: IPersonAddress[] = [{ id: 456 }];
          expectedResult = service.addPersonAddressToCollectionIfMissing(personAddressCollection, personAddress);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personAddress);
        });

        it('should add only unique PersonAddress to an array', () => {
          const personAddressArray: IPersonAddress[] = [{ id: 123 }, { id: 456 }, { id: 18977 }];
          const personAddressCollection: IPersonAddress[] = [{ id: 123 }];
          expectedResult = service.addPersonAddressToCollectionIfMissing(personAddressCollection, ...personAddressArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personAddress: IPersonAddress = { id: 123 };
          const personAddress2: IPersonAddress = { id: 456 };
          expectedResult = service.addPersonAddressToCollectionIfMissing([], personAddress, personAddress2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personAddress);
          expect(expectedResult).toContain(personAddress2);
        });

        it('should accept null and undefined values', () => {
          const personAddress: IPersonAddress = { id: 123 };
          expectedResult = service.addPersonAddressToCollectionIfMissing([], null, personAddress, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personAddress);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
