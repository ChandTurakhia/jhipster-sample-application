import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AddressHeaderService } from '../service/address-header.service';

import { AddressHeaderComponent } from './address-header.component';

describe('Component Tests', () => {
  describe('AddressHeader Management Component', () => {
    let comp: AddressHeaderComponent;
    let fixture: ComponentFixture<AddressHeaderComponent>;
    let service: AddressHeaderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AddressHeaderComponent],
      })
        .overrideTemplate(AddressHeaderComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AddressHeaderComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AddressHeaderService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.addressHeaders?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
