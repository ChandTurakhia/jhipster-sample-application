import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LocationHeaderService } from '../service/location-header.service';

import { LocationHeaderComponent } from './location-header.component';

describe('Component Tests', () => {
  describe('LocationHeader Management Component', () => {
    let comp: LocationHeaderComponent;
    let fixture: ComponentFixture<LocationHeaderComponent>;
    let service: LocationHeaderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocationHeaderComponent],
      })
        .overrideTemplate(LocationHeaderComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocationHeaderComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LocationHeaderService);

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
      expect(comp.locationHeaders?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
