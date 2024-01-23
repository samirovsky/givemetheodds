import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-odds-calculator',
  templateUrl: './odds-calculator.component.html',
  styleUrls: ['./odds-calculator.component.css']
})
export class OddsCalculatorComponent {
  selectedFile?: File;
  selectedFileName?: string;
  odds?: number;

  constructor(private http: HttpClient) {}

  onFileSelected(event: any) {
    this.selectedFile = <File>event.target.files[0];
    this.selectedFileName = this.selectedFile.name ?? null;
  }

  calculateOdds() {
    const formData = new FormData();
    formData.append('empireData', this.selectedFile as (string | Blob), this.selectedFile?.name);

    this.http.post('http://localhost:8080/journey/odds/calculate', formData)
      .subscribe(response => {
        this.odds = response as number;
      }, error => {
        console.error("Error calculating odds", error);
      });
  }
}
