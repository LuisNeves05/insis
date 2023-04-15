namespace VotesWrite.Entities;

public class HephaestusResponse <T>
{

    public T? Data { get; set; }
    public bool Success { get; set; } = true;
    public int TotalRecords {get; set; } = 0;
    public List<string> Error {get; set; } = new List<string>();

    public HephaestusResponse<T> SetFail(Exception ex){
        return new HephaestusResponse<T>(){
                Success = false,
                TotalRecords = 0,
                Error = new List<string>(){ex.ToString()}
        };
    }

    public HephaestusResponse<T> SetSucess(T data, int records){
        return new HephaestusResponse<T>(){
                Data = data,
                Success = true,
                TotalRecords = records
        };
    }

    public HephaestusResponse<T> SetSucess(){
        return new HephaestusResponse<T>(){
                Success = true
        };
    }
}